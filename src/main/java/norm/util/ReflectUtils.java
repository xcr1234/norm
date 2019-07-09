package norm.util;

import norm.exception.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static <T> T newInstance(Class<T> clz){
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            throw new ReflectionException("cannot instance object for class:" + clz.getName() + ",nested exception is :" + e , e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("cannot instance object for class:" + clz.getName() + ",nested exception is :" + e, e);
        }
    }

    public static Method getMethodOrNull(Class<?> clz,String name, Class<?>... parameterTypes){
        try {
            return clz.getDeclaredMethod(name,parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Class<?> getClassOrNull(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Object invoke(Method method,Object object,Object... args){
        try {
            return method.invoke(object,args);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("cannot invoke method:" + method + ",nested exception is :" + e , e);
        } catch (InvocationTargetException e) {
            throw new ReflectionException("cannot invoke method:" + method + ",nested exception is :" + e.getCause() , e.getCause());
        }
    }

    public static Object invokes(Method method,Object object,Object... args) throws Throwable{
        try{
            return method.invoke(object,args);
        }catch (InvocationTargetException e){
            throw e.getCause();
        }
    }

    public static boolean inClasspath(String name){
        try {
            Class.forName(name,false,ReflectUtils.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
