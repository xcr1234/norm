package org.norm.support.mybatis;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.norm.anno.Query;
import org.norm.anno.UpdateQuery;
import org.norm.core.interceptor.CrudDaoImpl;
import org.norm.core.interceptor.CrudDaoInterceptor;
import org.norm.util.ReflectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MyBatisMapperSupport implements MethodInterceptor {

    //mybatis的mapper对象
    private Object mapper;
    //norm的dao对象
    private CrudDaoImpl dao;

    private CrudDaoInterceptor interceptor;

    public MyBatisMapperSupport(Object mapper, CrudDaoImpl dao) {
        this.mapper = mapper;
        this.dao = dao;
        this.interceptor = new CrudDaoInterceptor(dao);
    }

    @Override
    public Object intercept(Object obj, Method interfaceMethod, Object[] args, MethodProxy proxy) throws Throwable {
        Method normMethod = null;
        if("getMapper".equals(interfaceMethod.getName()) && interfaceMethod.getParameterTypes().length == 0){
            return mapper;
        }
        if("getNorm".equals(interfaceMethod.getName()) && interfaceMethod.getParameterTypes().length == 0){
            return dao.getNorm();
        }
        if(interfaceMethod.isAnnotationPresent(Query.class)){
            Query query = interfaceMethod.getAnnotation(Query.class);
            return interceptor.handleQuery(query.sql(),interfaceMethod.getReturnType(),args);
        }
        if(interfaceMethod.isAnnotationPresent(UpdateQuery.class)){
            UpdateQuery updateQuery = interfaceMethod.getAnnotation(UpdateQuery.class);
            return interceptor.handleUpdateQuery(updateQuery.sql(),interfaceMethod.getReturnType(),args);
        }
        try{
            normMethod = CrudDaoImpl.class.getDeclaredMethod(interfaceMethod.getName(),interfaceMethod.getParameterTypes());
        }catch (NoSuchMethodException e){
            return invokeMybatis(interfaceMethod,args);
        }
        if(Modifier.isPublic(normMethod.getModifiers()) ){
            return invokeNorm(normMethod,args);
        }
        return invokeMybatis(interfaceMethod,args);
    }

    private Object invokeNorm(Method normMethod, Object[] args) throws Throwable{
        return ReflectUtils.invokeAndThrow(normMethod,dao,args);
    }

    private Object invokeMybatis(Method interfaceMethod, Object[] args) throws Throwable{
        return ReflectUtils.invokeAndThrow(interfaceMethod,mapper,args);
    }
}
