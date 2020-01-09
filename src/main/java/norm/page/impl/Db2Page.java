package norm.page.impl;

import norm.page.Page;
import norm.page.PageModel;
import norm.page.PageSql;

public class Db2Page implements PageSql {
    @Override
    public PageModel buildSql(Page page, String sql) {
        if(page.offset() == 0){
            return new PageModelImpl(sql + " fetch first ? rows only",page.limit());
        }
        return new PageModelImpl("select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( "
                + sql + " fetch first ? rows only ) as inner2_ ) as inner1_ where rownumber_ > ? order by rownumber_",
               page.limit(),page.offset());
    }


    @Override
    public String database() {
        return "db2";
    }
}
