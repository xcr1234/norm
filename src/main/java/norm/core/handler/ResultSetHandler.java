package norm.core.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public interface ResultSetHandler<T> {

    T handle(ResultSet resultSet, ResultSetMetaData md) throws SQLException;

    boolean requiresResultSetMetaData(ResultSet resultSet) throws SQLException;
}
