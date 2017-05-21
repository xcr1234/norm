package norm.page;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLPageFactory extends AbstractPageFactory {
    @Override
    public Page create(int pageNumber, int pageSize) {
        return new MySQLPage(pageNumber,pageSize);
    }

    protected static class MySQLPage extends AbstractPage{

        private static final long serialVersionUID = 4076892658120426348L;

        public MySQLPage(int pageNumber, int pageSize) {
            super(pageNumber, pageSize);
        }

        @Override
        public String pageSelect(String sql) {
            return sql + " limit ?,?" ;
        }

        @Override
        public void setState(PreparedStatement ps, int index) throws SQLException {
            ps.setInt(index,offset());
            ps.setInt(index+1,limit());
        }
    }
}
