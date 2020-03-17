package norm.core.handler;

import norm.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SingleValueResultSetHandler<T> implements ResultSetHandler<T>{

    private Class<T> type;

    public SingleValueResultSetHandler(Class<T> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T handle(ResultSet resultSet, ResultSetMetaData metaData) throws SQLException {
        return (T) JdbcUtils.getObject(resultSet,1,type);
    }

    @Override
    public boolean requiresResultSetMetaData(ResultSet resultSet) throws SQLException {
        return false;
    }
}
