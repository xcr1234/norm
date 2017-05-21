package norm.util;


import java.io.Serializable;
import java.lang.reflect.Method;

public class BeanUtils {
    public static boolean isGetter(Method method){
        return method.getName().startsWith("get") && method.getName().length() > 3 && method.getReturnType() != Void.class;
    }

    public static String getEntity(Method method){
        String mname = method.getName();
        if(isGetter(method) || isSetter(method)){
            char c = method.getName().charAt(3);
            if(mname.length() > 4){
                return Character.toLowerCase(c) + mname.substring(4);
            }else{
                return String.valueOf(Character.toLowerCase(c));
            }
        }
        return null;
    }

    public static String setMethod(String name){
        if(name.length() > 1){
            return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }else{
            return "set" + Character.toUpperCase(name.charAt(0));
        }
    }

    public static String getMethod(String name){
        if(name.length() > 1){
            return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }else{
            return "get" + Character.toUpperCase(name.charAt(0));
        }
    }

    public static boolean isSetter(Method method) {
        return method.getName().startsWith("set") && method.getName().length() > 3 && method.getParameterTypes().length == 1;
    }

    public static boolean isSerializable(Class<?> clazz){
        return Serializable.class.isAssignableFrom(clazz);
    }

    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBaseClass(Class clz){
        return clz.isPrimitive() || isWrapClass(clz);
    }

}
