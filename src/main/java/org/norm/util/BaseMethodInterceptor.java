package org.norm.util;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public abstract class BaseMethodInterceptor implements MethodInterceptor {
    @Override
    public final Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if(method.getDeclaringClass() == Object.class){
            if("hashCode()|equals()|toString()|finalize()".contains(method.getName())){
                return proxy.invokeSuper(obj,args);
            }
        }
        return invokeProxy(obj, method, args, proxy);
    }

    protected abstract Object invokeProxy(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable;
}
