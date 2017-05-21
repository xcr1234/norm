package norm.page;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OraclePageFactory extends AbstractPageFactory {
    @Override
    public Page create(int pageNumber, int pageSize) {
        return new OraclePage(pageNumber,pageSize);
    }

    protected static class OraclePage extends AbstractPage{

        private static final long serialVersionUID = 332748017217183130L;

        public OraclePage(int pageNumber, int pageSize) {
            super(pageNumber, pageSize);
        }

        @Override
        public String pageSelect(String sql) {
            StringBuilder pagingSelect = new StringBuilder( sql.length()+100 );
            if(offset() > 0){
                pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
            }else{
                pagingSelect.append("select * from ( ");
            }
            pagingSelect.append(sql);
            if(offset() > 0){
                pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
            }else{
                pagingSelect.append(" ) where rownum <= ?");
            }
            return pagingSelect.toString();
        }

        @Override
        public void setState(PreparedStatement ps, int index) throws SQLException {
            if(offset() > 0){
                ps.setInt(index,to());
                ps.setInt(index+1,from());
            }else{
                ps.setInt(index,limit());
            }
        }
    }
}
