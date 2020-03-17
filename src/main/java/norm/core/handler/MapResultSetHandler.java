package norm.core.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapResultSetHandler implements ResultSetHandler<Map<String, Object>> {

    @Override
    public Map<String, Object> handle(ResultSet rs, ResultSetMetaData md) throws SQLException {
        Map<String, Object> rowData = new HashMap<String, Object>();
        for (int i = 1; i <= md.getColumnCount(); i++) {
            rowData.put(md.getColumnLabel(i), rs.getObject(i));
        }
        return rowData;
    }

    @Override
    public boolean requiresResultSetMetaData(ResultSet resultSet) throws SQLException {
        return true;
    }
}
