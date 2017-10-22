package norm.page.impl;

import norm.page.Page;
import norm.page.PageSql;

public class Db2Page implements PageSql {
    @Override
    public String buildSql(Page page, String sql) {
        if(page.offset() == 0){
            return sql + " fetch first " + page.limit() + " rows only";
        }
        return "select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( "
                + sql + " fetch first " + page.limit() + " rows only ) as inner2_ ) as inner1_ where rownumber_ > "
                + page.offset() + " order by rownumber_";
    }


    @Override
    public String database() {
        return "db2";
    }
}
