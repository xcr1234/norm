package norm.page.impl;
import norm.page.Page;
import norm.page.PageSql;
public class MySQLPage implements PageSql {
    @Override
    public String buildSql(Page page, String sql) {
        return sql + " limit "+ page.offset() + "," + page.limit() ;
    }

    @Override
    public String database() {
        return "mysql";
    }
}