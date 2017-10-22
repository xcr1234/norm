package norm.page.impl;
import norm.page.Page;
import norm.page.PageSql;
public class OraclePage implements PageSql {
    @Override
    public String buildSql(Page page, String sql) {
        StringBuilder pagingSelect = new StringBuilder( sql.length()+100 );
        if(page.offset() > 0){
            pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        }else{
            pagingSelect.append("select * from ( ");
        }
        pagingSelect.append(sql);
        if(page.offset() > 0){
            pagingSelect.append(" ) row_ where rownum <= " + page.to() + ") where rownum_ > " + page.from());
        }else{
            pagingSelect.append(" ) where rownum <= " + page.limit());
        }
        return pagingSelect.toString();
    }



    @Override
    public String database() {
        return "oracle";
    }
}
