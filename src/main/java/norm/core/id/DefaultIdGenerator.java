package norm.core.id;

import norm.core.query.ReturnGenerateId;
import norm.util.JdbcUtils;

import java.sql.*;

public class DefaultIdGenerator implements IdGenerator {


    @Override
    public PreparedStatement prepareStatement(Connection connection, String sql, ReturnGenerateId returnGenerateId) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    public Object getValue(PreparedStatement ps, ReturnGenerateId returnGenerateId) throws SQLException {
        ResultSet rs = null;
        try {
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return JdbcUtils.getObject(rs, 1, returnGenerateId.getIdColumn().getType());
            }
            return null;
        } finally {
            JdbcUtils.closeObjects(null, rs, null);
        }
    }


}
