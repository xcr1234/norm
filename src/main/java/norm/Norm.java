package norm;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import norm.core.executor.ExecutorFactory;
import norm.core.generator.DefaultGeneratorFactory;
import norm.core.generator.QueryGenerator;
import norm.core.generator.QueryGeneratorFactory;
import norm.core.interceptor.CrudProxyImpl;
import norm.core.interceptor.CrudDaoInterceptor;
import norm.core.interceptor.CrudProxy;
import norm.core.log.SqlLogger;
import norm.core.meta.Meta;
import norm.exception.DefaultExceptionTranslator;
import norm.exception.ExceptionTranslator;
import norm.exception.SpringExceptionTranslator;
import norm.naming.NameStrategy;
import norm.page.PageSql;
import norm.util.*;

import javax.sql.DataSource;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Norm {
    private ExceptionTranslator exceptionTranslator;
    private Configuration configuration = new Configuration();
    private QueryGeneratorFactory generatorFactory = new DefaultGeneratorFactory(this);
    private Map<Class, Object> daoCache = new HashMap<Class, Object>();
    private ThreadLocal<TransactionManager> managerThreadLocal = new ThreadLocal<TransactionManager>() {
        @Override
        protected TransactionManager initialValue() {
            return new TransactionManager();
        }
    };

    protected ExceptionTranslator initExceptionTranslator() {
        if (SpringExceptionTranslator.valid()) {
            return new SpringExceptionTranslator(this);
        } else {
            return new DefaultExceptionTranslator();
        }
    }

    public ExceptionTranslator getExceptionTranslator(){
        if(this.exceptionTranslator == null){
            this.exceptionTranslator = initExceptionTranslator();
        }
        return this.exceptionTranslator;
    }


    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public NameStrategy getTableNameStrategy() {
        return configuration.getTableNameStrategy();
    }

    public void setTableNameStrategy(NameStrategy tableNameStrategy) {
        configuration.setTableNameStrategy(tableNameStrategy);
    }

    public NameStrategy getColumnNameStrategy() {
        return configuration.getColumnNameStrategy();
    }

    public void setColumnNameStrategy(NameStrategy columnNameStrategy) {
        configuration.setColumnNameStrategy(columnNameStrategy);
    }


    public boolean isShowSql() {
        return configuration.isShowSql();
    }
    public void setShowSql(boolean showSql) {
        configuration.setShowSql(showSql);
    }

    public SqlLogger getSqlLogger() {
        return configuration.getSqlLogger();
    }

    public void setSqlLogger(SqlLogger sqlLogger) {
        configuration.setSqlLogger(sqlLogger);
    }

    public PageSql getPageSql() {
        return configuration.getPageSql();
    }

    public void setPageSql(PageSql pageSql) {
        configuration.setPageSql(pageSql);
    }

    public ExecutorFactory getExecutorFactory() {
        return configuration.getExecutorFactory();
    }

    public void setExecutorFactory(ExecutorFactory executorFactory) {
        configuration.setExecutorFactory(executorFactory);
    }

    public String getSchema() {
        return configuration.getSchema();
    }

    public void setSchema(String schema) {
        configuration.setSchema(schema);
    }

    public int getJdbcNullType() {
        return configuration.getJdbcNullType();
    }

    public void setJdbcNullType(int jdbcNullType) {
        configuration.setJdbcNullType(jdbcNullType);
    }

    public DataSource getDataSource() {
        return configuration.getDataSource();
    }

    public void setDataSource(DataSource dataSource) {
        configuration.setDataSource(dataSource);
    }

    public QueryGeneratorFactory getGeneratorFactory() {
        return generatorFactory;
    }

    public void setGeneratorFactory(QueryGeneratorFactory generatorFactory) {
        this.generatorFactory = generatorFactory;
    }

    public SQLFormatter getSqlFormatter() {
        return configuration.getSqlFormatter();
    }

    public void setSqlFormatter(SQLFormatter sqlFormatter) {
        configuration.setSqlFormatter(sqlFormatter);
    }

    public boolean isFormatSql() {
        return configuration.isFormatSql();
    }

    public void setFormatSql(boolean formatSql) {
        configuration.setFormatSql(formatSql);
    }

    public boolean isUpdateNulls() {
        return configuration.isUpdateNulls();
    }

    public void setUpdateNulls(boolean updateNulls) {
        configuration.setUpdateNulls(updateNulls);
    }

    /**
     * get current connection , never be null / closed
     * @return the current connection
     * @throws SQLException throws when create connection error.
     */
    public Connection openConnection() throws SQLException {
        TransactionManager manager = managerThreadLocal.get();
        Connection connection = manager.getConnection();
        if(JdbcUtils.connClosed(connection)){
            ErrorContext.instance().setState("open connection");
            connection = configuration.newConnection();
            manager.setConnection(connection);
            if(manager.isBegin()){
                connection.setAutoCommit(false);
            }
        }
        return connection;
    }

    public RuntimeException handleError(Exception e) {
        TransactionManager transactionManager = managerThreadLocal.get();
        Connection connection = transactionManager.getConnection();
        if (!JdbcUtils.connClosed(connection) && transactionManager.isBegin()) {
            transactionManager.setBegin(false);
            try {
                connection.rollback();
                return getExceptionTranslator().translate("rollback success.", e);
            } catch (SQLException e1) {
                return getExceptionTranslator().translate("rollback failed :" + e1, e);
            }
        }
        return getExceptionTranslator().translate(null, e);
    }


    public void releaseConnection(Connection connection) {
        TransactionManager manager = managerThreadLocal.get();
        if(!manager.isBegin()){
            JdbcUtils.close(connection);
            manager.setConnection(null);
        }
    }

    public boolean isBegin(){
        return managerThreadLocal.get().isBegin();
    }

    public void begin() {
        TransactionManager manager = managerThreadLocal.get();
        manager.setBegin(true);
    }

    public void close(){
        TransactionManager manager = managerThreadLocal.get();
        manager.setConnection(null);
        manager.setBegin(false);
        Connection connection = manager.getConnection();
        JdbcUtils.close(connection);
    }



    public void commit(){
        TransactionManager transactionManager = managerThreadLocal.get();
        Connection connection = transactionManager.getConnection();
        transactionManager.setBegin(false);
        if(!JdbcUtils.connClosed(connection)){
            ErrorContext.instance().setState("commit transaction");
            try {
                connection.commit();
            } catch (SQLException e) {
                throw handleError(e);
            }finally {
                releaseConnection(connection);
            }
        }
    }

    public void rollback() {
        TransactionManager transactionManager = managerThreadLocal.get();
        transactionManager.setBegin(false);
        Connection connection = transactionManager.getConnection();
        if(!JdbcUtils.connClosed(connection)){
            ErrorContext.instance().setState("rollback transaction");
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw getExceptionTranslator().translate(null, e);
            }
        }

    }

    /**
     * get the current connection,may be null or closed.
     * @return the current connection
     */
    public Connection getCurrentConnection(){
        return getCurrentTransactionManager().getConnection();
    }

    public TransactionManager getCurrentTransactionManager(){
        return managerThreadLocal.get();
    }

    @SuppressWarnings("unchecked")
    public <Dao extends CrudDao<T, I>, T, I> Dao createDao(Class<Dao> daoClass) {
        Args.notNull(daoClass, "daoClass");
        if (!daoClass.isInterface()) {
            throw new IllegalArgumentException("dao class : '" + daoClass + "' should be an interface.");
        }
        if (!CrudDao.class.isAssignableFrom(daoClass)) {
            throw new IllegalArgumentException("illegal dao :" + daoClass + ",dao interface class should extends CrudDao<T,ID>");
        }
        Type[] types = daoClass.getGenericInterfaces();
        for (Type t : types) {
            if (t instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) t;
                Type[] arguments = pt.getActualTypeArguments();
                if (arguments.length == 2) {
                    Type t0 = arguments[0], t1 = arguments[1];
                    if (t0 instanceof Class && t1 instanceof Class && BeanUtils.isSerializable((Class<?>) t1)) {
                        Object dao = daoCache.get(daoClass);
                        if (dao == null) {
                            try {
                                dao = createDao(daoClass, (Class<T>) t0, (Class<I>) t1);
                                daoCache.put(daoClass, dao);
                            } catch (ClassCastException e) {
                                throw new IllegalArgumentException("illegal dao:" + daoClass);
                            }
                        }
                        return (Dao) dao;
                    }
                }
            }
        }
        throw new IllegalArgumentException("illegal dao:" + daoClass + ",dao interface class should extends CrudDao<T,ID>");

    }

    @SuppressWarnings("unchecked")
    private <Dao extends CrudDao<T, I>, T, I> Dao createDao(Class<Dao> daoClass, Class<T> tClass, Class<I> idClass) {
        if (Modifier.isFinal(tClass.getModifiers()) || Modifier.isAbstract(tClass.getModifiers())) {
            throw new IllegalArgumentException("entity class can't be final or abstract :" + tClass);
        }
        Meta meta = Meta.parse(tClass, configuration);
        if (meta.getIdColumn().getType() != idClass) {
            throw new IllegalArgumentException("illegal bean,id type mismatch :" + tClass + ",dao:" + daoClass);
        }
        CrudProxy crudDao = createCrudProxy(generatorFactory.getGenerator(tClass));
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{daoClass,NormAware.class});
        enhancer.setCallbacks(new Callback[]{new CrudDaoInterceptor(crudDao), NoOp.INSTANCE});
        enhancer.setCallbackFilter(MethodFilter.getInstance());
        return (Dao) enhancer.create();
    }

    public CrudProxy createProxyForDaoType(Class daoType){
        Type [] types = daoType.getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Class beanClass = (Class) parameterizedType.getActualTypeArguments()[0];
        return createCrudProxy(generatorFactory.getGenerator(beanClass));
    }

    protected CrudProxy createCrudProxy(QueryGenerator generator){
        return new CrudProxyImpl(this, generator);
    }

}
