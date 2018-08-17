package org.norm.util;

import org.norm.exception.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static <T> T newInstance(Class<T> clz){
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            throw new ReflectionException("cannot instance object for class:" + clz.getName() , e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("cannot instance object for class:" + clz.getName() , e);
        }
    }

    public static Method getMethodOrNull(Class<?> clz,String name, Class<?>... parameterTypes){
        try {
            return clz.getDeclaredMethod(name,parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Object invoke(Method method,Object object,Object... args){
        try {
            return method.invoke(object,args);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("cannot invoke method:" + method , e);
        } catch (InvocationTargetException e) {
            throw new ReflectionException("cannot invoke method:" + method , e.getCause());
        }
    }
}
