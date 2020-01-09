package norm.page.impl;


import norm.page.Page;
import norm.page.PageModel;
import norm.page.PageSql;

public class H2Page implements PageSql {
    @Override
    public PageModel buildSql(Page page, String sql) {
        return new PageModelImpl(sql + " limit ? offset ?",
                page.limit(),page.offset());
    }


    @Override
    public String database() {
        return "h2";
    }
}
