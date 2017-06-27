package norm.impl;


import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import norm.BeanException;
import norm.Norm;
import norm.QueryException;
import norm.anno.Query;
import norm.anno.UpdateQuery;
import norm.page.Page;
import norm.result.QueryResult;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public final class DaoInterceptor implements MethodInterceptor {

    private CrudDaoImpl dbDao;
    private Meta meta;
    private Class daoClass;
    private Norm norm;

    public DaoInterceptor(Class c, Class daoClass, Norm norm){
        meta = Meta.parse(c,norm.getConfiguration());
        this.daoClass = daoClass;
        this.norm = norm;
        dbDao = CrudDaoImpl.create(c,norm);
    }


    @Override
    public Object intercept(Object self, Method thisMethod, Object[] args, MethodProxy proxy) throws Throwable {
        if(thisMethod.isAnnotationPresent(Query.class)){
            Query query = thisMethod.getAnnotation(Query.class);
            if(thisMethod.getReturnType() == QueryResult.class){
                Page Page = null;
                Object[] params = null;
                if(args.length > 0){
                    if(args[args.length-1] instanceof Page){
                        Page = (Page) args[args.length-1];
                        params = new Object[args.length -1];
                        System.arraycopy(args, 0, params, 0, args.length - 1);
                    }else{
                        Page = null;
                        params = args;
                    }
                }else{
                    Page = null;
                    params = args;
                }
                return dbDao.queryResult(query.sql(),Page,params);
            }
            if(thisMethod.getReturnType() == List.class){
                Page Page = null;
                Object[] params = null;
                if(args.length > 0){
                    if(args[args.length-1] instanceof Page){
                        Page = (Page) args[args.length-1];
                        params = new Object[args.length -1];
                        System.arraycopy(args, 0, params, 0, args.length - 1);
                    }else{
                        Page = null;
                        params = args;
                    }
                }else{
                    Page = null;
                    params = args;
                }
                return dbDao.listBySql(query.sql(),Page,params);
            }else if(thisMethod.getReturnType() == meta.getClazz()){
                return dbDao.findBySql(query.sql(),args);
            }else{
                try {
                    return dbDao.findValueBySql(query.sql(),args,thisMethod.getReturnType(),null);
                }catch (QueryException e){
                    if(e.getMessage() != null && e.getMessage().startsWith("unsupported sql type")){
                        throw new BeanException("illegal dao : "+daoClass+" ,method:"+thisMethod+",@Query method 's return-type doesn't support:" + thisMethod.getReturnType());
                    }
                    throw e;
                }
            }
        }
        if(thisMethod.isAnnotationPresent(UpdateQuery.class)){
            UpdateQuery updateQuery = thisMethod.getAnnotation(UpdateQuery.class);
            Class returnType = thisMethod.getReturnType();
            if(returnType == Void.class || returnType == void.class){
                dbDao.updateQuery(updateQuery.sql(),args);
                return null;
            }else if(returnType == int.class || returnType == Integer.class){
                return dbDao.updateQuery(updateQuery.sql(),args);
            }else if(returnType == boolean.class || returnType == Boolean.class){
                return dbDao.updateQuery(updateQuery.sql(),args) > 0;
            }else{
                throw new BeanException("illegal dao:" + daoClass+",invalid @UpdateQuery method "+thisMethod+" ,return type:"+returnType);
            }
        }
        try {
            //如果是dbDao中的实现方法.
            Method method = CrudDaoImpl.class.getDeclaredMethod(thisMethod.getName(),thisMethod.getParameterTypes());
            try {
                return method.invoke(dbDao,args);
            }catch (InvocationTargetException e){
                throw e.getCause() != null ? e.getCause() : e;
            }
        }catch (NoSuchMethodException e){
            return proxy.invokeSuper(self,args);
        }
    }
}
