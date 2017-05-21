package norm.page;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public class H2PageFactory extends AbstractPageFactory{
    @Override
    public Page create(int pageNumber, int pageSize) {
        return new H2Page(pageNumber,pageSize);
    }

    protected static class H2Page extends AbstractPage{

        private static final long serialVersionUID = -7130028361776075079L;

        public H2Page(int pageNumber, int pageSize) {
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
