package norm;

import norm.cache.CacheManager;
import norm.naming.TableNameStrategy;
import norm.util.Args;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Norms中维护了一个全局的{@link norm.Norm}静态变量
 */
public final class Norms  {

    private Norms(){}

    private static Norm norm = new Norm();

    public static Norm getNorm() {
        assert  norm != null;
        return norm;
    }

    public static void setNorm(Norm norm) {
        Args.notNull(norm,"default norm");
        Norms.norm = norm;
    }

    public static void closeConnection() {
        norm.closeConnection();
    }

    public static Object enableCache(Object object) {
        return norm.enableCache(object);
    }

    public static <Service> Service createService(Class<Service> serviceClass) {
        return norm.createService(serviceClass);
    }

    public static boolean isFormat_sql() {
        return norm.isFormat_sql();
    }

    public static void setFormat_sql(boolean format_sql) {
        norm.setFormat_sql(format_sql);
    }

    public static boolean isShow_sql() {
        return norm.isShow_sql();
    }

    public static void setShow_sql(boolean show_sql) {
        norm.setShow_sql(show_sql);
    }

    public static void showSQL(String sql) {
        norm.showSQL(sql);
    }

    public static Transactional getTransactional() {
        return norm.getTransactional();
    }

    public static void setConfiguration(Configuration configuration) {
        norm.setConfiguration(configuration);
    }

    public static Configuration getConfiguration() {
        return norm.getConfiguration();
    }

    public static <Dao extends CrudDao<T, I>, T, I> Dao createDao(Class<Dao> daoClass) {
        return norm.createDao(daoClass);
    }

    public static Connection getConnection() {
        return norm.getConnection();
    }

    public static String getSchema() {
        return norm.getSchema();
    }

    public static Norm setSchema(String schema) {
        return norm.setSchema(schema);
    }

    public static SQLLogger getSqlLogger() {
        return norm.getSqlLogger();
    }

    public static Norm setSqlLogger(SQLLogger sqlLogger) {
       return norm.setSqlLogger(sqlLogger);
    }

    public static TableNameStrategy getTableNameStrategy() {
        return norm.getTableNameStrategy();
    }

    public static Norm setTableNameStrategy(TableNameStrategy tableNameStrategy) {
        return norm.setTableNameStrategy(tableNameStrategy);
    }

    public static CacheManager getCacheManager() {
        return norm.getCacheManager();
    }

    public static Norm setCacheManager(CacheManager cacheManager) {
        return norm.setCacheManager(cacheManager);
    }

    public static Properties getInfo() {
        return norm.getInfo();
    }

    public static Norm setInfo(Properties info) {
        return norm.setInfo(info);
    }

    public static DataSource getDataSource() {
        return norm.getDataSource();
    }

    public static Norm setDataSource(DataSource dataSource) {
        return norm.setDataSource(dataSource);
    }

    public static String getDriverClass() {
        return norm.getDriverClass();
    }

    public static Norm setDriverClass(String driverClass) {
        return norm.setDriverClass(driverClass);
    }

    public static int getMaxRecursion() {
        return norm.getMaxRecursion();
    }

    public static Norm setMaxRecursion(int maxRecursion) {
        return norm.setMaxRecursion(maxRecursion);
    }

    public static String getUrl() {
        return norm.getUrl();
    }

    public static Norm setUrl(String url) {
        return norm.setUrl(url);
    }

    public static String getUsername() {
        return norm.getUsername();
    }

    public static Norm setUsername(String username) {
        return norm.setUsername(username);
    }

    public static String getPassword() {
        return norm.getPassword();
    }

    public static Norm setPassword(String password) {
        return norm.setPassword(password);
    }

    public static boolean isFormatSql() {
        return norm.isFormatSql();
    }

    public static Norm setFormatSql(boolean formatSql) {
        return norm.setFormatSql(formatSql);
    }

    public static boolean isShowSql() {
        return norm.isShowSql();
    }

    public static Norm setShowSql(boolean showSql) {
        return norm.setShowSql(showSql);
    }


    public static SQLFormatter getSqlFormatter() {
        return norm.getSqlFormatter();
    }

    public static String getDatabase() {
        return norm.getDatabase();
    }

    public static void setDatabase(String database) {
        norm.setDatabase(database);
    }

    public static Norm setSqlFormatter(SQLFormatter sqlFormatter) {
        return norm.setSqlFormatter(sqlFormatter);
    }
}
