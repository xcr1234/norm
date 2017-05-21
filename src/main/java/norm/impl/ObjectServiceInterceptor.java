package norm.impl;


import net.sf.cglib.proxy.MethodProxy;
import norm.Norm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectServiceInterceptor extends ServiceInterceptor{

    private Object object;

    public ObjectServiceInterceptor(Object o, Norm norm) {
        super(o.getClass(), norm);
        this.object = o;
    }

    @Override
    protected Object invokeSuper(Object self, Method thisMethod, MethodProxy proxy, Object[] args) throws Throwable {
        try {
            Method method = object.getClass().getDeclaredMethod(thisMethod.getName(),thisMethod.getParameterTypes());
            try {
                return method.invoke(object,args);
            }catch (InvocationTargetException ex){
                throw ex.getCause();
            }
        }catch (NoSuchMethodException e){
            return proxy.invokeSuper(self,args);
        }
    }
}
