package norm;

/**
 * 设置如何show sql语句的策略，默认为System.out.println
 */
public interface SQLLogger {
    void logSQL(String sql);
}
