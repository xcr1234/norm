package norm.page;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQLServer分页 ，只支持SQLServer 2005及以上
 */
public class SQLServerPageFactory extends AbstractPageFactory {
    @Override
    public Page create(int pageNumber, int pageSize) {
        return new SQLServerPage(pageNumber,pageSize);
    }

    protected static class SQLServerPage extends AbstractPage{

        private static final long serialVersionUID = -1446424483378571211L;

        public SQLServerPage(int pageNumber, int pageSize) {
            super(pageNumber, pageSize);
        }

        @Override
        public String pageSelect(String querySelect) {
            int lastIndexOfOrderBy = getLastIndexOfOrderBy(querySelect);
            //　没有 order by 或第一页的情况下
            if(lastIndexOfOrderBy < 0 || querySelect.endsWith(")") || offset() == 0){
                StringBuilder sb = new StringBuilder(querySelect.length() + 8);
                sb.append(querySelect);
                sb.insert(getSqlAfterSelectInsertPoint(querySelect)," top " + limit());
                return sb.toString();
            }else{
                //取出 order by 语句
                String orderby = querySelect.substring(lastIndexOfOrderBy, querySelect.length());
                //取出 from 前的内容
                int indexOfFrom = querySelect.toLowerCase().indexOf("from");
                String selectFld = querySelect.substring(0,indexOfFrom);
                //取出 from 语句后的内容
                String selectFromTableAndWhere = querySelect.substring(indexOfFrom, lastIndexOfOrderBy);
                return "select * from (" +
                        selectFld +
                        ",ROW_NUMBER() OVER(" + orderby + ") as _page_row_num_norm " +
                        selectFromTableAndWhere + " ) temp " +
                        " where  _page_row_num_norm BETWEEN  " +
                        (offset() + 1) + " and " + limit();
            }
        }

        @Override
        public void setState(PreparedStatement ps, int index) throws SQLException {

        }

        private static int getLastIndexOfOrderBy(String sql){
            return sql.toLowerCase().lastIndexOf("order by ");
        }

        private static int getSqlAfterSelectInsertPoint(String sql){
            int selectIndex = sql.toLowerCase().indexOf("select");

            final int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");

            return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
        }
    }
}
