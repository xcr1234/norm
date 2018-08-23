package org.norm.core.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.norm.anno.Query;
import org.norm.core.query.SelectQuery;
import org.norm.core.query.UpdateQuery;
import org.norm.core.handler.SingleValueResultSetHandler;
import org.norm.core.parameter.ArrayParameters;
import org.norm.exception.BeanException;
import org.norm.exception.ExecutorException;
import org.norm.page.Page;
import org.norm.util.ReflectUtils;

import java.lang.reflect.Method;
import java.util.List;

public class CrudDaoInterceptor implements MethodInterceptor {

    private CrudDaoImpl crudDao;
    private Class<?> beanClass;

    public CrudDaoInterceptor(CrudDaoImpl crudDao) {
        this.crudDao = crudDao;
        this.beanClass = crudDao.getBeanClass();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if("getNorm".equals(method.getName()) && method.getParameterTypes().length == 0){
            return crudDao.getNorm();
        }
        Query query = method.getAnnotation(Query.class);
        org.norm.anno.UpdateQuery updateQuery = method.getAnnotation(org.norm.anno.UpdateQuery.class);
        if(query != null){
            return handleQuery(query.sql(), method.getReturnType(), args);
        }else if(updateQuery != null){
            return handleUpdateQuery(updateQuery.sql(),method.getReturnType(),args);
        }
        Method thisMethod = ReflectUtils.getMethodOrNull(CrudDaoImpl.class,method.getName(),method.getParameterTypes());
        if(thisMethod != null){
            //如果是dbDao中的实现方法.
            return ReflectUtils.invokeAndThrow(thisMethod,crudDao,args);
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
        selectQuery.setParameters(new ArrayParameters(params));
        if(returnType == List.class ){
            selectQuery.setResultSetHandler(crudDao.getGenerator().getResultSetHandler());
            if(page == null){
                return crudDao.selectList(selectQuery);
            }else{
                return crudDao.selectPage(selectQuery,page);
            }
        }else if(returnType ==  beanClass){
            selectQuery.setResultSetHandler(crudDao.getGenerator().getResultSetHandler());
        }else{
            selectQuery.setResultSetHandler(new SingleValueResultSetHandler(returnType));
        }
        return crudDao.selectOne(selectQuery);
    }

    public Object handleUpdateQuery(String sql,final Class returnType, Object[] args){
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setSql(sql);
        updateQuery.setParameters(new ArrayParameters(args));
        if(returnType == Void.class || returnType == void.class){
            crudDao.executeUpdate(updateQuery);
        }else if(returnType == int.class || returnType == Integer.class){
            return crudDao.executeUpdate(updateQuery);
        }else if(returnType == boolean.class || returnType == Boolean.class){
            return crudDao.executeUpdate(updateQuery) > 0;
        }
        throw new ExecutorException("@UpdateQuery method return type should be void/int/boolean , :" + returnType.getName());
    }

}
