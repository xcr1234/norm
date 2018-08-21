package org.norm;


import org.norm.core.executor.ExecutorFactory;
import org.norm.core.executor.DefaultExecutorFactory;
import org.norm.core.generator.QueryGeneratorFactory;
import org.norm.core.log.SqlLogger;
import org.norm.core.log.DefaultSqlLogger;
import org.norm.exception.ExecutorException;
import org.norm.naming.DefaultTableNameStrategy;
import org.norm.naming.NameStrategy;
import org.norm.page.PageSql;
import org.norm.util.ReflectUtils;
import org.norm.util.sql.BasicFormatterImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

public class Configuration {
    private boolean showSql;
    private boolean formatSql;
    private PageSql pageSql;
    private SqlLogger sqlLogger = new DefaultSqlLogger();
    private SQLFormatter sqlFormatter = new BasicFormatterImpl();
    private ExecutorFactory executorFactory = new DefaultExecutorFactory();
    private NameStrategy tableNameStrategy = new DefaultTableNameStrategy();
    private NameStrategy columnNameStrategy = new DefaultTableNameStrategy();
    private String schema;
    private int jdbcNullType = Types.OTHER;

    private boolean updateNulls = true;

    //connection configurations:
    private DataSource dataSource;
    private String driverClass;
    private String url;
    private String username;
    private String password;
    private Properties connProperties;

    public Properties getConnProperties() {
        return connProperties;
    }

    public void setConnProperties(Properties connProperties) {
        this.connProperties = connProperties;
    }

    private boolean driverFlag;

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

    public boolean isFormatSql() {
        return formatSql;
    }

    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
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

    public boolean isUpdateNulls() {
        return updateNulls;
    }

    public void setUpdateNulls(boolean updateNulls) {
        this.updateNulls = updateNulls;
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

    public Connection newConnection() throws SQLException{
        if(dataSource != null){
            if(username != null && password != null){
                return dataSource.getConnection(username,password);
            }
            return dataSource.getConnection();
        }
        if(driverClass != null && !driverFlag){
            Class<?> clazz = ReflectUtils.getClassOrNull(driverClass);
            if(clazz != null){
                driverFlag = true;
            }else{
                throw new ExecutorException("register driver failed , class not found :" + driverClass);
            }
        }
        if(url == null){
            throw new SQLException("the jdbc url is null!");
        }
        Properties properties = null;
        if(connProperties != null){
            properties = new Properties(connProperties);
        }else{
            properties = new Properties();
        }
        if(username != null){
            properties.put("user",username);
        }
        if(password != null){
            properties.put("password",password);
        }
        return DriverManager.getConnection(url,properties);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SQLFormatter getSqlFormatter() {
        return sqlFormatter;
    }

    public void setSqlFormatter(SQLFormatter sqlFormatter) {
        this.sqlFormatter = sqlFormatter;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        driverFlag = false;
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
