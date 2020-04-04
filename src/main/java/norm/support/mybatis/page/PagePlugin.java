package norm.support.mybatis.page;

import norm.core.parameter.Parameter;
import norm.core.parameter.ValueParameter;
import norm.page.Page;
import norm.page.PageModel;
import norm.page.PageSql;
import norm.page.impl.*;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.PluginException;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * MyBatis的分页插件，放在这里给大家使用
 */
public class PagePlugin implements Interceptor {

    private static Map<String,PageSql> pageSqlMap = new HashMap<String,PageSql>();


    private String database;


    public static void registerPageSql(PageSql pageSql){
        if(pageSql == null){
            throw new IllegalArgumentException();
        }
        String db = pageSql.database();
        if(db == null){
            throw new PluginException("the database of pagesql is null!");
        }
        pageSqlMap.put(db.toLowerCase(),pageSql);
    }


    static {
        registerPageSql(new Db2Page());
        registerPageSql(new DerbyPage());
        registerPageSql(new H2Page());
        registerPageSql(new MySQLPage());
        registerPageSql(new OraclePage());
        registerPageSql(new PostgreSQLPage());
        registerPageSql(new SQLServerPage());
    }



    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Invocation invocation) throws Throwable {
        Page page = PageHelper.getPage();
        if(page == null){
            return invocation.proceed();
        }
        if(invocation.getTarget() instanceof StatementHandler){
            StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
            MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
            // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
            // 可以分离出最原始的的目标类)
            while (metaStatementHandler.hasGetter("h")) {
                Object object = metaStatementHandler.getValue("h");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            // 分离最后一个代理对象的目标类
            while (metaStatementHandler.hasGetter("target")) {
                Object object = metaStatementHandler.getValue("target");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
            //判断是不是SELECT操作 并且跳过存储过程
            if (SqlCommandType.SELECT != mappedStatement.getSqlCommandType()
                    || StatementType.CALLABLE == mappedStatement.getStatementType()) {
                return invocation.proceed();
            }
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            String sql = boundSql.getSql();
            Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
            PageModel pageModel = this.buildPageSql(page,sql);
            //1.计算page count
            this.evalPageCount(sql, invocation, mappedStatement, boundSql, page);
            //2.重写分页sql
            metaStatementHandler.setValue("delegate.boundSql.sql", pageModel.getSql());
            //3.重写参数映射parameterMappings，将参数值写到additionalParameters中
            List<ParameterMapping> origin = (List<ParameterMapping>) metaStatementHandler.getValue("delegate.boundSql.parameterMappings");
            List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>(origin.size() + 2);
            parameterMappings.addAll(origin);
            Map<String, Object> additionalParameters = (Map<String, Object>) metaStatementHandler.getValue("delegate.boundSql.additionalParameters");
            if(pageModel.getFirstParameter() != null){
                ValueParameter first = pageModel.getFirstParameter();
                parameterMappings.add(
                        new ParameterMapping.Builder(configuration,first.getName(),first.getValue().getClass())
                                .build());
                additionalParameters.put(first.getName(),first.getValue());
            }
            if(pageModel.getSecondParameter() != null){
                ValueParameter second = pageModel.getSecondParameter();
                parameterMappings.add(
                        new ParameterMapping.Builder(configuration,second.getName(),second.getValue().getClass())
                                .build());
                additionalParameters.put(second.getName(),second.getValue());
            }
            metaStatementHandler.setValue("delegate.boundSql.parameterMappings", parameterMappings);
            return invocation.proceed();
        }else if(invocation.getTarget() instanceof ResultSetHandler){
            return invocation.proceed();
        }
        //如果抛出这个异常 你可以去买彩票了
        throw new IllegalStateException("invocation error!");
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler || target instanceof ResultSetHandler) {
            Class<?> type = target.getClass();
            Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(type);
            Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
            if (interfaces.length > 0) {
                return Proxy.newProxyInstance(
                        type.getClassLoader(),
                        interfaces,
                        new Plugin(target, this, signatureMap));
            }
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        String database = properties.getProperty("database");
        if(database == null || database.isEmpty()){
            throw new RuntimeException("null database property!");
        }
        this.database = database;
    }

    private PageModel buildPageSql(Page page, String sql){
        PageSql pageSql = pageSqlMap.get(this.database.toLowerCase());
        if(pageSql == null){
            throw new PluginException("not support database :" + this.database);
        }
        return pageSql.buildSql(page,sql);
    }

    private void evalPageCount(String sql, Invocation invocation, MappedStatement mappedStatement,
                               BoundSql boundSql, Page page){
        if(page.isEvalCount()){
            String countSql = "select count(*) from ( " + sql + " ) count_";
            Connection connection = (Connection) invocation.getArgs()[0];
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                ps = connection.prepareStatement(countSql);
                BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                        boundSql.getParameterMappings(), boundSql.getParameterObject());
                setParameters(ps, mappedStatement, countBS, boundSql.getParameterObject());
                rs = ps.executeQuery();
                int totalCount = 0;
                if (rs.next()) {
                    totalCount = rs.getInt(1);
                }
                page.setTotal(totalCount);
                int pageSize = page.getPageSize();
                page.setPageCount(totalCount / pageSize + ((totalCount % pageSize == 0) ? 0 : 1));
            }catch (SQLException e){
                throw new PersistenceException("eval page count error.sql:" + countSql,e);
            }finally {
                if(rs != null){
                    try {
                        rs.close();
                    } catch (SQLException e) {

                    }
                }
                if(ps != null){
                    try {
                        ps.close();
                    } catch (SQLException e) {

                    }
                }
            }

        }
    }

    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
                               Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }


    //这里跟mybatis原生的Plugin.wrap方法原理类似，不过这里是采取手动代理的方式，而不是采取注解
    //这样，可兼容mybatis所有版本，具体参考mybatis中Plugin类的源码
    private Map<Class<?>, Set<Method>> getSignatureMap(Class<?> type){
        Map<Class<?>, Set<Method>> signatureMap = new HashMap<Class<?>, Set<Method>>();
        //处理StatementHandler prepare方法
        handleMethodSet(signatureMap, StatementHandler.class,"prepare");
        //处理ResultSetHandler handleResultSets方法
        handleMethodSet(signatureMap, ResultSetHandler.class,"handleResultSets");
        return signatureMap;
    }

    private static void handleMethodSet(Map<Class<?>, Set<Method>> signatureMap,Class<?> type,String methodName){
        Method[] methods = type.getDeclaredMethods();
        Set<Method> set = new HashSet<Method>();
        for(Method method : methods){
            if(method.getName().equals(methodName)){
                set.add(method);
            }
        }
        if(set.isEmpty()){
            throw new PluginException("no such method on [" + type + "] name:" + methodName);
        }
        signatureMap.put(type,set);
    }


    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        while (type != null) {
            for (Class<?> c : type.getInterfaces()) {
                if (signatureMap.containsKey(c)) {
                    interfaces.add(c);
                }
            }
            type = type.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }


    private static class Plugin implements InvocationHandler {
        private final Object target;
        private final Interceptor interceptor;
        private final Map<Class<?>, Set<Method>> signatureMap;

        public Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
            this.target = target;
            this.interceptor = interceptor;
            this.signatureMap = signatureMap;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                Set<Method> methods = signatureMap.get(method.getDeclaringClass());
                if (methods != null && methods.contains(method)) {
                    return interceptor.intercept(new Invocation(target, method, args));
                }
                return method.invoke(target, args);
            } catch (Exception e) {
                throw ExceptionUtil.unwrapThrowable(e);
            }
        }
    }


}
