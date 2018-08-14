package norm;


import java.sql.Connection;
import java.util.List;

/**
 * 在数据库查询的过程中遇到的异常。包括sql查询时的异常，获取数据库连接时抛出的异常。
 */
public class QueryException extends RuntimeException{
    private static final long serialVersionUID = 2854051742050429886L;


    public QueryException(String message) {
        super(message);
    }

    public QueryException(Throwable cause) {
        super(cause);
    }

    public QueryException(String message, Throwable cause) {
        super(message + ";\n nested exception is " + cause, cause);
    }

    private Class<?> type;

    private String sql;

    private List<?> parameters;

    private Connection connection;

    private Norm norm;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<?> getParameters() {
        return parameters;
    }

    public void setParameters(List<?> parameters) {
        this.parameters = parameters;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Norm getNorm() {
        return norm;
    }

    public void setNorm(Norm norm) {
        this.norm = norm;
    }
}
