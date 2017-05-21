package norm.page;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgreSQLPageFactory extends AbstractPageFactory{
    @Override
    public Page create(int pageNumber, int pageSize) {
        return new PostgreSQLPage(pageNumber,pageSize);
    }

    protected static class PostgreSQLPage extends AbstractPage{

        private static final long serialVersionUID = -8744322736875899821L;

        public PostgreSQLPage(int pageNumber, int pageSize) {
            super(pageNumber, pageSize);
        }

        @Override
        public String pageSelect(String sql) {
            return sql + " limit ? offset ?";
        }

        @Override
        public void setState(PreparedStatement ps, int index) throws SQLException {
            ps.setInt(index,limit());
            ps.setInt(index+1,offset());
        }
    }
}
