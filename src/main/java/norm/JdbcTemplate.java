package norm;

import norm.core.handler.ResultSetHandler;

import java.util.List;
import java.util.Map;

/**
 * JdbcTemplate是JDBC的封装，目的是使JDBC更加易于使用
 *
 * @param <T>
 */
public interface JdbcTemplate<T> {


    String selectFields();

    String table();

    String selectSql();

    T getType();

    ResultSetHandler<T> getResultSetHandler();

    /**
     * 执行`INSERT`、`UPDATE`、`DELETE`等DML语句。
     *
     * @param sql
     * @param args
     * @return
     */
    int update(String sql, Object... args);

    <R> R queryOne(String sql, ResultSetHandler<R> handler, Object... args);

    <R> R queryOne(String sql, Class<R> type, Object... args);

    T queryOne(String sql, Object... args);

    <R> List<R> queryList(String sql, ResultSetHandler<R> handler, Object... args);

    <R> List<R> queryList(String sql, Class<R> type, Object... args);

    List<T> queryList(String sql, Object... args);

    List<Map<String, Object>> queryMap(String sql, Object... args);



}
