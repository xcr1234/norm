package org.norm.util;

import org.norm.exception.QueryException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ExceptionUtils {


    public static QueryException parse(Exception cause){
        Throwable t = cause;
        if(cause instanceof InvocationTargetException){
            t = ((InvocationTargetException) cause).getTargetException();
        }
        ErrorContext errorContext = ErrorContext.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("an error occurred when execute jdbc query,nested exception is :").append(t).append('\n');
        sb.append("this error happened when ").append(errorContext.getState()).append('\n');
        sb.append("sql:").append(errorContext.getSql()).append('\n');
        sb.append("params:");
        showParameters(sb,errorContext.getParams());
        sb.append('\n');
        if(errorContext.getParameter() != null){
            sb.append("current property:").append(errorContext.getParameter().getProperty()).append('\n');
        }
        if(cause instanceof QueryException){
            return new QueryException(sb.toString());
        }
        return new QueryException(sb.toString(),cause);
    }

    private static void showParameters(StringBuilder sb,List<?> args){
        if(args == null){
            sb.append("[]");
            return;
        }
        sb.append("[");
        boolean first = true;
        for(Object arg : args){
            if(arg == null){
                sb.append("(null)");
            }else{
                String clz = arg.getClass().getName();
                if(clz.startsWith("java.lang.")){
                    clz =  arg.getClass().getSimpleName();
                }
                sb.append(arg).append('(').append(clz).append(')');
            }
            if(!first)sb.append(" , ");
            first = false;
        }
    }

}
