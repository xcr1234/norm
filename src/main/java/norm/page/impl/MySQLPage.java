package norm.page.impl;
import norm.page.Page;
import norm.page.PageModel;
import norm.page.PageSql;
public class MySQLPage implements PageSql {
    @Override
    public PageModel buildSql(Page page, String sql) {
        return new PageModelImpl(sql + " limit ?,?",page.offset(),page.limit()) ;
    }

    @Override
    public String database() {
        return "mysql";
    }
}
