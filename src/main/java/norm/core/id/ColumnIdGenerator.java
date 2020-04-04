package norm.core.id;

import norm.core.query.ReturnGenerateId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ColumnIdGenerator extends DefaultIdGenerator {

    @Override
    public PreparedStatement prepareStatement(Connection connection, String sql, ReturnGenerateId returnGenerateId) throws SQLException {
        return connection.prepareStatement(sql, new String[]{returnGenerateId.getIdColumn().getColumnName()});
    }
}
