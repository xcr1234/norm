package org.norm.util;

import org.norm.exception.QueryException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ExceptionUtils {


    public static QueryException wrap(Exception cause){
        Throwable t = cause;
        if(cause instanceof InvocationTargetException){
            t = ((InvocationTargetException) cause).getTargetException();
        }
        ErrorContext errorContext = ErrorContext.instance();
        StringBuilder sb = new StringBuilder();
        sb.append("an error occurred when execute jdbc query,nested exception is :").append(t).append('\n');
        sb.append("this error happened at state: ").append(errorContext.getState()).append('\n');
        sb.append("sql: ").append(errorContext.getSql()).append('\n');
        sb.append("params: ");
        showParameters(sb,errorContext.getParams());
        sb.append('\n');
        if(errorContext.getParameter() != null){
            sb.append("current parameter:").append(errorContext.getParameter().getErrInfo()).append('\n');
        }
        return new QueryException(sb.toString(),cause);
    }

    public static QueryException wrap(String transInfo,Exception cause){
        Throwable t = cause;
        if(cause instanceof InvocationTargetException){
            t = ((InvocationTargetException) cause).getTargetException();
        }
        ErrorContext errorContext = ErrorContext.instance();
        StringBuilder sb = new StringBuilder();
        sb.append("an error occurred when execute jdbc query,nested exception is :").append(t).append('\n');
        sb.append("this error happened at state: ").append(errorContext.getState()).append('\n');
        sb.append("sql: ").append(errorContext.getSql()).append('\n');
        sb.append("params: ");
        showParameters(sb,errorContext.getParams());
        sb.append('\n');
        if(errorContext.getParameter() != null){
            sb.append("current parameter:").append(errorContext.getParameter().getErrInfo()).append('\n');
        }
        sb.append("transaction status:").append(transInfo).append('\n');
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
            if(!first)sb.append(" , ");
            if(arg == null){
                sb.append("(null)");
            }else{
                String clz = arg.getClass().getName();
                if(clz.startsWith("java.lang.")){
                    clz =  arg.getClass().getSimpleName();
                }
                sb.append(arg).append('(').append(clz).append(')');
            }
            first = false;
        }
        sb.append(']');
    }

}
