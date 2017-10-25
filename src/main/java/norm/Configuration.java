package norm;


import norm.cache.CacheManager;
import norm.impl.DefaultSQLLogger;
import norm.naming.DefaultTableNameStrategy;
import norm.naming.NameStrategy;
import norm.util.Args;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Configuration  {
    private boolean formatSql;
    private boolean showSql;
    private DataSource dataSource;
    private String driverClass;
    private String schema;
    private String url;
    private String username;
    private String password;
    private Properties info;
    private NameStrategy tableNameStrategy;
    private NameStrategy columnNameStrategy = DefaultTableNameStrategy.DEFAULT;
    private SQLLogger sqlLogger;
    boolean driverRegistered;
    private int maxRecursion = 3;
    private CacheManager cacheManager;
    private SQLFormatter sqlFormatter;
    private String database;

    //oracle不支持自增id，这个必须设置为false，否则insert会保错
    private boolean collectGenerateId = true;

    public Configuration() {
    }



    public Configuration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Configuration(String driverClass, String url, String username, String password) {
        this.driverClass = driverClass;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Configuration(String driverClass, String url, Properties info) {
        this.driverClass = driverClass;
        this.url = url;
        this.info = info;
    }

    public Properties getInfo() {
        return info;
    }

    public void setInfo(Properties info) {
        this.info = info;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        driverRegistered = false;
        this.driverClass = driverClass;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public int getMaxRecursion() {
        return maxRecursion;
    }

    public void setMaxRecursion(int maxRecursion) {
        this.maxRecursion = maxRecursion;
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

    public boolean isFormatSql() {
        return formatSql;
    }

    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public Connection getConnection() throws SQLException{
        if(dataSource != null){
            if(username != null || password != null){
                return dataSource.getConnection(username,password);
            }
            return dataSource.getConnection();
        }
        if(url != null){
            if(info != null){
                return DriverManager.getConnection(url,info);
            }
            return DriverManager.getConnection(url,username,password);
        }
        throw new QueryException("the connection of the Configuration hasn't be configured!");
    }

    void registerDriver() throws ClassNotFoundException {
        if(driverClass != null){
            Class.forName(driverClass);
            driverRegistered = true;
        }
    }

    public SQLLogger getSqlLogger() {
        return sqlLogger == null ? DefaultSQLLogger.getInstance() : sqlLogger;
    }

    public void setSqlLogger(SQLLogger sqlLogger) {
        Args.notNull(sqlLogger,"sql logger");
        this.sqlLogger = sqlLogger;
    }

    public NameStrategy getTableNameStrategy() {
        return tableNameStrategy == null ? DefaultTableNameStrategy.DEFAULT : tableNameStrategy;
    }

    public void setTableNameStrategy(NameStrategy tableNameStrategy) {
        Args.notNull(tableNameStrategy,"table naming strategy");
        this.tableNameStrategy = tableNameStrategy;
    }

    boolean isDriverRegistered() {
        return driverRegistered;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public SQLFormatter getSqlFormatter() {
        return sqlFormatter;
    }

    public void setSqlFormatter(SQLFormatter sqlFormatter) {
        Args.notNull(sqlFormatter,"sql formatter");
        this.sqlFormatter = sqlFormatter;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        Args.notNull(database,"the database");
        this.database = database.toLowerCase();
    }

    public NameStrategy getColumnNameStrategy() {
        return columnNameStrategy;
    }

    public void setColumnNameStrategy(NameStrategy columnNameStrategy) {
        Args.notNull(columnNameStrategy,"column naming");
        this.columnNameStrategy = columnNameStrategy;
    }

    public boolean isCollectGenerateId() {
        return collectGenerateId;
    }

    public void setCollectGenerateId(boolean collectGenerateId) {
        this.collectGenerateId = collectGenerateId;
    }
}
