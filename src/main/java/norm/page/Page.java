package norm.page;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Page {

    /**
     * 封装分页查询sql语句
     * @param sql 原始sql语句
     * @return 封装后的sql语句
     */
    String pageSelect(String sql);

    /**
     * 填充分页查询PreparedStatement
     * @param ps 待填充的PreparedStatement
     * @param index 起始编号
     * @throws SQLException 接收填充过程中抛出的SQLException，一并处理
     */
    void setState(PreparedStatement ps, int index) throws SQLException;

}
