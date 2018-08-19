package org.norm.page.impl;

import org.norm.page.Page;
import org.norm.page.PageSql;

public class SQLServerPage implements PageSql {
    @Override
    public String buildSql(Page page, String querySelect) {
        int lastIndexOfOrderBy = getLastIndexOfOrderBy(querySelect);
        //　没有 order by 或第一页的情况下
        if(lastIndexOfOrderBy < 0 || querySelect.endsWith(")") || page.offset() == 0){
            StringBuilder sb = new StringBuilder(querySelect.length() + 8);
            sb.append(querySelect);
            sb.insert(getSqlAfterSelectInsertPoint(querySelect)," top " + page.limit());
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
                    (page.offset() + 1) + " and " + page.limit();
        }
    }


    @Override
    public String database() {
        return "sqlserver";
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
