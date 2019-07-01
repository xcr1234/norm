package norm.util;

public class AssertUtils {

    public static void notNull(Object arg,String m){
        if(arg == null){
            throw new IllegalArgumentException(m + " can't be null!");
        }
    }

}
