package norm.core.id;

import norm.core.query.ReturnGenerateId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IdGenerator {
    PreparedStatement prepareStatement(Connection connection, String sql, ReturnGenerateId returnGenerateId) throws SQLException;

    Object getValue(PreparedStatement ps, ReturnGenerateId returnGenerateId) throws SQLException;
}
