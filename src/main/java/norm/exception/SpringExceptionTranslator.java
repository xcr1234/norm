package norm.exception;

import norm.Norm;
import norm.util.ErrorContext;
import norm.util.ExceptionUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.SQLException;

public class SpringExceptionTranslator implements ExceptionTranslator {

    public static boolean valid() {
        try {
            Class.forName("org.springframework.jdbc.support.SQLExceptionTranslator");
            return true;
        } catch (Exception e) {
            return false;
        }
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
                return getExceptionTranslator().translate(errorContext.getState() + "(transaction status:" + transInfo + ")" ,errorContext.getSql(),s);
            }
        }
        if (transInfo == null) {
            return ExceptionUtils.wrap(e);
        }
        return ExceptionUtils.wrap(transInfo, e);
    }


}
