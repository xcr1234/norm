package norm.support.mybatis;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import norm.anno.Query;
import norm.anno.UpdateQuery;
import norm.core.interceptor.CrudDaoInterceptor;
import norm.core.interceptor.CrudProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MyBatisDaoSupport implements MethodInterceptor {

    //mybatis的mapper对象
    private Object mapper;
    //norm的dao对象
    private CrudProxy crudProxy;

    private CrudDaoInterceptor crudDaoInterceptor;

    public MyBatisDaoSupport(Object mapper, CrudProxy crudProxy) {
        this.mapper = mapper;
        this.crudProxy = crudProxy;
        this.crudDaoInterceptor = new CrudDaoInterceptor(crudProxy);
    }

    @Override
    public Object intercept(Object self, Method interfaceMethod, Object[] args, MethodProxy proxy) throws Throwable {
        if(interfaceMethod.getName().equals("__getMapper") && interfaceMethod.getParameterTypes().length == 0){
            return this.mapper;
        }
        if(interfaceMethod.isAnnotationPresent(InvokeMyBatis.class)){
            return handleByMyBatis(interfaceMethod,args);
        }
        Method normMethod = null;
        if(interfaceMethod.isAnnotationPresent(Query.class)){
            Query query = interfaceMethod.getAnnotation(Query.class);
            return this.crudDaoInterceptor.handleQuery(query.sql(),interfaceMethod.getReturnType(),args);
        }
        if(interfaceMethod.isAnnotationPresent(UpdateQuery.class)){
            UpdateQuery query = interfaceMethod.getAnnotation(UpdateQuery.class);
            return this.crudDaoInterceptor.handleUpdateQuery(query.sql(),interfaceMethod.getReturnType(),args);
        }
        try{
            normMethod = crudProxy.getClass().getDeclaredMethod(interfaceMethod.getName(),interfaceMethod.getParameterTypes());
        }catch (NoSuchMethodException e){
            return handleByMyBatis(interfaceMethod,args);
        }
        if(Modifier.isPublic(normMethod.getModifiers()) ){
            return handleByNorm(normMethod,args);
        }
        return handleByMyBatis(interfaceMethod,args);

    }

    private Object handleByNorm(Method normMethod,Object[] args) throws Throwable{
        try{
            return normMethod.invoke(crudProxy,args);
        }catch (InvocationTargetException e){
            throw e.getCause();
        }
    }

    private Object handleByMyBatis(Method interfaceMethod,Object[] args) throws Throwable{
        Method mybatisMethod = mapper.getClass().getDeclaredMethod(interfaceMethod.getName(),interfaceMethod.getParameterTypes());
        try{
            return mybatisMethod.invoke(mapper,args);
        }catch (InvocationTargetException e){
            throw e.getCause();
        }
    }




}
