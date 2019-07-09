package norm.exception;

import norm.Norm;
import norm.util.ErrorContext;
import norm.util.ExceptionUtils;
import norm.util.ReflectUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.SQLException;

public class SpringExceptionTranslator implements ExceptionTranslator {

    private static final String SPRING_TRANSLATOR_CLASS = "org.springframework.jdbc.support.SQLExceptionTranslator";

    public static boolean valid() {
        return ReflectUtils.inClasspath(SPRING_TRANSLATOR_CLASS);
    }

    private SQLExceptionTranslator exceptionTranslator;
    private Norm norm;

    public SpringExceptionTranslator(Norm norm) {
        this.norm = norm;

    }

    public SQLExceptionTranslator getExceptionTranslator(){
        if(this.exceptionTranslator == null){
            DataSource dataSource = norm.getDataSource();
            if (dataSource != null) {
                this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            } else {
                this.exceptionTranslator = new SQLStateSQLExceptionTranslator();
            }
        }
        return this.exceptionTranslator;
    }


    @Override
    public RuntimeException translate(String transInfo, Exception e) {
        if(e instanceof SQLException){
            SQLException s = (SQLException)e;
            ErrorContext errorContext = ErrorContext.instance();
            if(transInfo == null){
                return getExceptionTranslator().translate(errorContext.getState(), errorContext.getSql(), s);
            }else{
                return getExceptionTranslator().translate(errorContext.getState() + "(" + transInfo + ")" ,errorContext.getSql(),s);
            }
        }
        if (transInfo == null) {
            return ExceptionUtils.wrap(e);
        }
        return ExceptionUtils.wrap(transInfo, e);
    }


}
