package norm.impl;


import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import norm.BeanException;
import norm.Norm;
import norm.anno.*;
import norm.anno.cache.Evict;
import norm.anno.cache.EvictAll;
import norm.anno.cache.Put;
import norm.anno.cache.Cache;
import norm.cache.CacheManager;
import ognl.Ognl;
import ognl.OgnlException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ServiceInterceptor implements MethodInterceptor {

    private Class serviceClass;
    private Norm norm;

    public ServiceInterceptor(Class serviceClass, Norm norm) {
        this.serviceClass = serviceClass;
        this.norm = norm;
    }


    @Override
    public Object intercept(Object self, Method thisMethod, Object[] args, MethodProxy proxy) throws Throwable {
        if("__getNormObject".equals(thisMethod.getName()) && Norm.class.equals(thisMethod.getReturnType())){
            return norm;
        }
        boolean trans = false;
        try {
            if(thisMethod.isAnnotationPresent(Transaction.class)){
                Transaction transaction = thisMethod.getAnnotation(Transaction.class);
                String condition = transaction.condition();
                if (condition.isEmpty() || filter(condition, thisMethod, args, null)) {
                    trans = true;
                    norm.getTransactional().begin();
                }
            }
            return invoke0(self,thisMethod,proxy,args);
        }finally {
            if(trans){
                norm.getTransactional().commit();
            }
        }
    }



    private String createKey(String key,Method method,Object args[],Object result) {
        /*if(args.length == 0){
            return key;
        }*/
        if(key.isEmpty()){
            return method.getName();
        }
        Map<String,Object> context = createOgnlContext(method,args);
        if(result != null){
            context.put("_result_",result);
        }
        try {
            return String.valueOf(Ognl.getValue(key,context));
        } catch (OgnlException e) {
            throw new BeanException("illegal service "+ serviceClass + ",invalid key expression",e);
        }

    }

    private boolean filter(String condition,Method method,Object args[],Object result) {
        if(condition.isEmpty()){
            return true;
        }
        Map<String , Object> context = createOgnlContext(method,args);
        if(result != null){
            context.put("_result_",result);
        }
        try {
            Object value = Ognl.getValue(condition,context);
            if(!(value instanceof Boolean)){
                throw new BeanException("illegal service "+ serviceClass + ",condition \"" + condition + "\" should return boolean value.");
            }
            return (Boolean)value;
        } catch (OgnlException e) {
            throw new BeanException("illegal service "+ serviceClass + ",invalid key expression",e);
        }
    }



    private Map<String , Object> createOgnlContext(Method method,Object[] args){
        Map<String , Object> context = new HashMap<String , Object>();
        for(int i=0;i<args.length;i++){
            context.put("arg"+i,args[i]);
        }
        Annotation[][] annotations = method.getParameterAnnotations();
        for(int i=0;i<annotations.length;i++){
            for(Annotation annotation:annotations[i]){
                if(annotation instanceof Name){
                    context.put(((Name) annotation).value(),args[i]);
                    break;
                }
            }
        }
        return context;
    }

    protected Object invokeSuper(Object self, Method thisMethod, MethodProxy proxy, Object[] args) throws Throwable{
        return proxy.invokeSuper(self,args);
    }

    private  Object invoke0(Object self, Method thisMethod, MethodProxy proxy, Object[] args) throws Throwable{
        Object value = null;
        CacheManager cacheManager = norm.getConfiguration().getCacheManager();
        if(cacheManager != null){
            //有Cacheable注解.
            if(thisMethod.isAnnotationPresent(Cache.class)){
                Cache cacheable = thisMethod.getAnnotation(Cache.class);

                String key = createKey(cacheable.key(),thisMethod,args,null);
                norm.cache.Cache cache = cacheManager.getCache(cacheable.value());

                if(cache != null && filter(cacheable.condition(),thisMethod,args,null)){  //可以从cache中取值
                    value = cache.get(key,thisMethod.getReturnType());
                    if(value != null){
                        return value;
                    }
                    value = invokeSuper(self, thisMethod, proxy, args);   //value中值为null或者class不匹配的情况
                    if(value != null){
                        cache.put(key,value);
                    }
                }else {
                    value = invokeSuper(self, thisMethod, proxy, args); //不满足从cache中取值的条件
                }
                return value;
            }

            //如果有Cacheput注解，则执行原方法，然后放入cache.
            if(thisMethod.isAnnotationPresent(Put.class)){
                Object thisValue = invokeSuper(self, thisMethod, proxy, args);//执行原方法
                Put cachePut = thisMethod.getAnnotation(Put.class);
                String key = createKey(cachePut.key(),thisMethod,args,thisValue);

                norm.cache.Cache cache = cacheManager.getCache(cachePut.value());
                if(cache != null && filter(cachePut.condition(),thisMethod,args,thisValue) && thisValue != null){
                    cache.put(key,thisValue);
                }
                return thisValue;
            }

            //如果有CacheEvict注解
            if(thisMethod.isAnnotationPresent(Evict.class)){
                Evict cacheEvict = thisMethod.getAnnotation(Evict.class);
                String key = createKey(cacheEvict.key(),thisMethod,args,null);
                norm.cache.Cache cache = cacheManager.getCache(cacheEvict.value());
                if(cache != null && filter(cacheEvict.condition(),thisMethod,args,null) && cacheEvict.beforeInvocation()){
                    if(cacheEvict.allEntries()){
                        cache.evictAll();
                    }else{
                        cache.evict(key);
                    }
                }
                Object thisValue = invokeSuper(self, thisMethod, proxy, args);   //执行原方法
                if(cache != null && filter(cacheEvict.condition(),thisMethod,args,thisValue) && !cacheEvict.beforeInvocation()){
                    key = createKey(cacheEvict.key(),thisMethod,args,thisValue);
                    if(cacheEvict.allEntries()){
                        cache.evictAll();
                    }else{
                        cache.evict(key);
                    }
                }
                return thisValue;
            }

            //如果有CacheEvictAll注解，则清除全部cache.
            if(thisMethod.isAnnotationPresent(EvictAll.class)){
                EvictAll cacheEvictAll = thisMethod.getAnnotation(EvictAll.class);
                if(cacheEvictAll.beforeInvocation()){
                    cacheManager.evictAll();
                }
                Object thisValue = invokeSuper(self, thisMethod, proxy, args);  //执行原方法
                if(!cacheEvictAll.beforeInvocation()){
                    cacheManager.evictAll();
                }
                return thisValue;
            }
        }
        return invokeSuper(self, thisMethod, proxy, args);
    }

}
