package norm.core.executor;

import norm.core.query.SelectQuery;
import norm.core.query.UpdateQuery;
import norm.page.Page;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Executor {
    int executeUpdate(Connection connection, UpdateQuery query)throws SQLException;

    <T> T selectOne(Connection connection,SelectQuery<T> query) throws SQLException;

    <T> List<T> selectList(Connection connection, SelectQuery<T> query) throws SQLException;

    <T> void processPage(Connection connection, SelectQuery<T> query, Page page)throws SQLException;
}
