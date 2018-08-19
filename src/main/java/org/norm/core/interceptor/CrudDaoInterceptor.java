package org.norm.core.interceptor;

import net.sf.cglib.proxy.MethodProxy;
import org.norm.anno.Query;
import org.norm.core.SelectQuery;
import org.norm.core.UpdateQuery;
import org.norm.core.handler.SingleValueResultSetHandler;
import org.norm.core.parameter.ArrayParameters;
import org.norm.exception.BeanException;
import org.norm.page.Page;
import org.norm.util.BaseMethodInterceptor;

import java.lang.reflect.Method;
import java.util.List;

public class CrudDaoInterceptor extends BaseMethodInterceptor {

    private CrudDaoImpl crudDao;
    private Class<?> daoClass;
    private Class<?> beanClass;

    public CrudDaoInterceptor(CrudDaoImpl crudDao) {
        this.crudDao = crudDao;
    }

    @Override
    protected Object invokeProxy(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Query query = method.getAnnotation(Query.class);
        org.norm.anno.UpdateQuery updateQuery = method.getAnnotation(org.norm.anno.UpdateQuery.class);
        if(query != null){
            return handleQuery(query, method, args);
        }else if(updateQuery != null){
            return handleUpdateQuery(updateQuery,method,args);
        }else{

        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Object handleQuery(Query anno, final Method thisMethod, Object[] args){
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
        selectQuery.setSql(anno.sql());
        selectQuery.setParameters(new ArrayParameters(params));
        final Class<?> returnType = thisMethod.getReturnType();
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

    private Object handleUpdateQuery(org.norm.anno.UpdateQuery anno,final Method thisMethod, Object[] args){
        Class returnType = thisMethod.getReturnType();
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setSql(anno.sql());
        updateQuery.setParameters(new ArrayParameters(args));
        if(returnType == Void.class || returnType == void.class){
            crudDao.executeUpdate(updateQuery);
        }else if(returnType == int.class || returnType == Integer.class){
            return crudDao.executeUpdate(updateQuery);
        }else if(returnType == boolean.class || returnType == Boolean.class){
            return crudDao.executeUpdate(updateQuery) > 0;
        }
        throw new BeanException("@UpdateQuery method return type should be void/int/boolean , :" + returnType.getName());
    }

}