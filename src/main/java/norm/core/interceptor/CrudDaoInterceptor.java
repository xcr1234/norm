package norm.core.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import norm.anno.Query;
import norm.core.query.SelectQuery;
import norm.core.query.UpdateQuery;
import norm.core.handler.SingleValueResultSetHandler;
import norm.core.parameter.ArrayParameters;
import norm.exception.ExecutorException;
import norm.page.Page;
import norm.util.ReflectUtils;

import java.lang.reflect.Method;
import java.util.List;

public class CrudDaoInterceptor implements MethodInterceptor {

    private CrudProxy crudProxy;
    private Class<?> beanClass;

    public CrudDaoInterceptor(CrudProxy crudProxy) {
        this.crudProxy = crudProxy;
        this.beanClass = crudProxy.getGenerator().getBeanClass();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if("getNorm".equals(method.getName()) && method.getParameterTypes().length == 0){
            return crudProxy.getNorm();
        }
        Query query = method.getAnnotation(Query.class);
        norm.anno.UpdateQuery updateQuery = method.getAnnotation(norm.anno.UpdateQuery.class);
        if(query != null){
            return handleQuery(query.sql(), method.getReturnType(), args);
        }else if(updateQuery != null){
            return handleUpdateQuery(updateQuery.sql(),method.getReturnType(),args);
        }
        Method thisMethod = ReflectUtils.getMethodOrNull(CrudProxyImpl.class,method.getName(),method.getParameterTypes());
        if(thisMethod != null){
            //如果是dbDao中的实现方法.
            return ReflectUtils.invokes(thisMethod,crudProxy,args);
        }
        return proxy.invokeSuper(obj,args);
    }

    @SuppressWarnings("unchecked")
    public Object handleQuery(String sql, final Class<?> returnType, Object[] args){
        Page page = null;
        Object[] params = null;
        if(args.length > 0 && args[args.length-1] instanceof Page){
            page = (Page) args[args.length-1];
            params = new Object[args.length -1];
            System.arraycopy(args, 0, params, 0, args.length - 1);
        }else{
            page = null;
            params = args;
        }
        SelectQuery selectQuery = new SelectQuery<Object>();
        selectQuery.setSql(sql);
        selectQuery.setParameters(new ArrayParameters(params,crudProxy.getNorm().getJdbcNullType()));
        if(returnType == List.class ){
            selectQuery.setResultSetHandler(crudProxy.getGenerator().getResultSetHandler());
            if(page == null){
                return crudProxy.selectList(selectQuery);
            }else{
                return crudProxy.selectPage(selectQuery,page);
            }
        }else if(returnType ==  beanClass){
            selectQuery.setResultSetHandler(crudProxy.getGenerator().getResultSetHandler());
        }else{
            selectQuery.setResultSetHandler(new SingleValueResultSetHandler(returnType));
        }
        return crudProxy.selectOne(selectQuery);
    }

    public Object handleUpdateQuery(String sql,final Class returnType, Object[] args){
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setSql(sql);
        updateQuery.setParameters(new ArrayParameters(args,crudProxy.getNorm().getJdbcNullType()));
        if(returnType == Void.class || returnType == void.class){
            crudProxy.executeUpdate(updateQuery);
        }else if(returnType == int.class || returnType == Integer.class){
            return crudProxy.executeUpdate(updateQuery);
        }else if(returnType == boolean.class || returnType == Boolean.class){
            return crudProxy.executeUpdate(updateQuery) > 0;
        }
        throw new ExecutorException("@UpdateQuery method return type should be void/int/boolean , :" + returnType.getName());
    }

}
