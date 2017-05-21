package norm;


/**
 * 在数据库查询的过程中遇到的异常。包括sql查询时的异常，获取数据库连接时抛出的异常。
 */
public class QueryException extends RuntimeException{
    private static final long serialVersionUID = 2854051742050429886L;

    public QueryException( String message) {
        super(message);
    }

    public QueryException(Throwable cause) {
        super(cause);
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
