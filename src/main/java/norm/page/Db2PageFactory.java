package norm.page;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Db2PageFactory extends AbstractPageFactory{

    @Override
    public Page create(int pageNumber, int pageSize) {
        return new Db2Page(pageNumber,pageSize);
    }

    protected static class Db2Page extends AbstractPage{


        private static final long serialVersionUID = 6994621723251934038L;

        public Db2Page(int pageNumber, int pageSize) {
            super(pageNumber, pageSize);
        }

        @Override
        public String pageSelect(String sql) {
            if(offset() == 0){
                return sql + " fetch first " + limit() + " rows only";
            }
            return "select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( "
                    + sql + " fetch first " + limit() + " rows only ) as inner2_ ) as inner1_ where rownumber_ > "
                    + offset() + " order by rownumber_";
        }

        @Override
        public void setState(PreparedStatement ps, int index) throws SQLException {

        }
    }


}
