package norm.page.impl;


import norm.page.Page;
import norm.page.PageSql;

public class H2Page implements PageSql {
    @Override
    public String buildSql(Page page, String sql) {
        return sql + " limit "+ page.limit() +" offset " + page.offset();
    }


    @Override
    public String database() {
        return "h2";
    }
}
