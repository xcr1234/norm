package org.norm.util;


public final class Args {


    public static void notNull(Object object,String m){
        if(object == null){
            throw new IllegalArgumentException(m + " can't be null!");
        }
    }

}
