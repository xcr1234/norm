package org.norm;


import org.norm.core.log.SqlLogger;
import org.norm.core.log.DefaultSqlLogger;
import org.norm.page.PageSql;

import java.sql.Connection;
import java.sql.SQLException;

public class Configuration {
    private boolean showSql;
    private PageSql pageSql;
    private SqlLogger sqlLogger = new DefaultSqlLogger();

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public SqlLogger getSqlLogger() {
        return sqlLogger;
    }

    public void setSqlLogger(SqlLogger sqlLogger) {
        this.sqlLogger = sqlLogger;
    }

    public PageSql getPageSql() {
        return pageSql;
    }

    public void setPageSql(PageSql pageSql) {
        this.pageSql = pageSql;
    }

    public Connection getConnection()throws SQLException{
        return null;
    }

    public Connection openConnection()throws SQLException{
        return null;
    }

    public void releaseConnection(Connection connection){

    }
}
