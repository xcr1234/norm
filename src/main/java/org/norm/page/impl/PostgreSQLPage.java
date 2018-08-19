package org.norm.page.impl;
import org.norm.page.Page;
import org.norm.page.PageSql;
public class PostgreSQLPage implements PageSql {
    @Override
    public String buildSql(Page page, String sql) {
        return sql + " limit "+page.limit()+" offset " + page.offset();
    }


    @Override
    public String database() {
        return "postgresql";
    }
}
