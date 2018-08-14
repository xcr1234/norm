package norm.support.mybatis;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import norm.BeanException;
import norm.CrudDao;
import norm.QueryException;
import norm.anno.Query;
import norm.anno.UpdateQuery;
import norm.impl.CrudDaoImpl;
import norm.impl.DaoInterceptor;
import norm.page.Page;
import norm.result.QueryResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.BindException;
import java.util.Arrays;
import java.util.List;

public class MyBatisDaoSupport implements MethodInterceptor {

    //mybatis的mapper对象
    private Object mapper;
    //norm的dao对象
    private CrudDaoImpl dao;

    public MyBatisDaoSupport(Object mapper, CrudDaoImpl dao) {
        this.mapper = mapper;
        this.dao = dao;
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
            return DaoInterceptor.executeQuery(interfaceMethod,args,dao,this.mapper);
        }
        if(interfaceMethod.isAnnotationPresent(UpdateQuery.class)){
            return DaoInterceptor.executeUpdateQuery(interfaceMethod,args,dao,this.mapper);
        }
        try{
            normMethod = CrudDaoImpl.class.getDeclaredMethod(interfaceMethod.getName(),interfaceMethod.getParameterTypes());
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
            return normMethod.invoke(dao,args);
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
