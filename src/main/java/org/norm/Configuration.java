package org.norm;


import org.norm.core.executor.ExecutorFactory;
import org.norm.core.executor.DefaultExecutorFactory;
import org.norm.core.log.SqlLogger;
import org.norm.core.log.DefaultSqlLogger;
import org.norm.naming.DefaultTableNameStrategy;
import org.norm.naming.NameStrategy;
import org.norm.page.PageSql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class Configuration {
    private boolean showSql;
    private PageSql pageSql;
    private SqlLogger sqlLogger = new DefaultSqlLogger();
    private ExecutorFactory executorFactory = new DefaultExecutorFactory();
    private NameStrategy tableNameStrategy = new DefaultTableNameStrategy();
    private NameStrategy columnNameStrategy = new DefaultTableNameStrategy();
    private String schema;
    private int jdbcNullType = Types.OTHER;

    public NameStrategy getTableNameStrategy() {
        return tableNameStrategy;
    }

    public void setTableNameStrategy(NameStrategy tableNameStrategy) {
        this.tableNameStrategy = tableNameStrategy;
    }

    public NameStrategy getColumnNameStrategy() {
        return columnNameStrategy;
    }

    public void setColumnNameStrategy(NameStrategy columnNameStrategy) {
        this.columnNameStrategy = columnNameStrategy;
    }

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

    public ExecutorFactory getExecutorFactory() {
        return executorFactory;
    }

    public void setExecutorFactory(ExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public int getJdbcNullType() {
        return jdbcNullType;
    }

    public void setJdbcNullType(int jdbcNullType) {
        this.jdbcNullType = jdbcNullType;
    }

    public Connection openConnection()throws SQLException{
        return null;
    }

    public void releaseConnection(Connection connection){

    }
}
