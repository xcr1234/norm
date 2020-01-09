package norm.page.impl;

import norm.page.Page;
import norm.page.PageModel;
import norm.page.PageSql;

public class OraclePage implements PageSql {
    @Override
    public PageModel buildSql(Page page, String sql) {
        Integer first = null ,second = null;
        StringBuilder pagingSelect = new StringBuilder( sql.length()+100 );
        if(page.offset() > 0){
            pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        }else{
            pagingSelect.append("select * from ( ");
        }
        pagingSelect.append(sql);
        if(page.offset() > 0){
            first = page.to();
            second = page.from();
            pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
        }else{
            first = page.limit();
            pagingSelect.append(" ) where rownum <= ?");
        }
        return new PageModelImpl(pagingSelect.toString(),first,second);
    }



    @Override
    public String database() {
        return "oracle";
    }
}
