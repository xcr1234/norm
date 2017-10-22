package norm.support.mybatis;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import norm.CrudDao;
import norm.impl.CrudDaoImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.BindException;
import java.util.Arrays;

public class MyBatisDaoSupport implements MethodInterceptor {

    //mybatis的mapper对象
    private Object mapper;
    //norm的dao对象
    private CrudDao dao;

    public MyBatisDaoSupport(Object mapper, CrudDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        try{
            try{
                Method normMethod = CrudDao.class.getDeclaredMethod(method.getName(),method.getParameterTypes());
                return normMethod.invoke(dao,args);
            }catch (InvocationTargetException exx){
                throw exx.getCause();
            }
        }catch (NoSuchMethodException e){
            Method mybatisMethod = mapper.getClass().getDeclaredMethod(method.getName(),method.getParameterTypes());
            try{
                return mybatisMethod.invoke(mapper,args);
            }catch (InvocationTargetException exx){
                throw exx.getCause();
            }
        }
    }
}
