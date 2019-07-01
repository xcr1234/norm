package norm.util;



import norm.core.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;

public class ErrorContext {

    public static final String EXECUTE_UPDATE = "execute update";
    public static final String EXECUTE_QUERY = "execute query";
    public static final String MAP_OBJECTS = "map objects";
    public static final String PAGE_EVAL = "page eval count";
    public static final String PREPARE_STATEMENT = "prepare statement";
    public static final String SET_PARAMETERS = "set parameters";


    private String sql;
    private List<Object> params;
    private String state;
    private Parameter parameter;

    private ErrorContext(){

    }

    private static ThreadLocal<ErrorContext> local = new ThreadLocal<ErrorContext>(){
        @Override
        protected ErrorContext initialValue() {
            return new ErrorContext();
        }
    };

    public static ErrorContext instance(){
        return local.get();
    }

    public static void clear(){
        local.remove();
    }


    public String getSql() {
        if(sql != null){
            return sql.replace("\n"," ").replace("\r"," ").replace("\t"," ");
        }
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
