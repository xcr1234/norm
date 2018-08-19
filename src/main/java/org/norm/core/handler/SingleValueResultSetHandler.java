package org.norm.core.handler;

import org.norm.core.ResultSetHandler;
import org.norm.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleValueResultSetHandler implements ResultSetHandler{

    private Class<?> type;

    public SingleValueResultSetHandler(Class<?> type) {
        this.type = type;
    }

    @Override
    public Object handle(ResultSet resultSet) throws SQLException {
        return JdbcUtils.getObject(resultSet,1,type);
    }
}
