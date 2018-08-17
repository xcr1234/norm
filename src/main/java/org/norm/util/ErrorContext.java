package org.norm.util;

import org.norm.result.Parameter;

import java.util.ArrayList;
import java.util.List;

public class ErrorContext {


    private String sql;
    private List<Object> params;
    private Parameter parameter;
    private String state;

    private ErrorContext(){

    }

    private static ThreadLocal<ErrorContext> local = new ThreadLocal<ErrorContext>(){
        @Override
        protected ErrorContext initialValue() {
            return new ErrorContext();
        }
    };

    public static ErrorContext getInstance(){
        return local.get();
    }

    public static void clear(){
        local.remove();
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getParams() {
        return params;
    }

    public void addParam(Object arg){
        if(params == null){
            params = new ArrayList<Object>();
        }
        params.add(arg);
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
