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
public final class Norms {
    private static Norm norm = new Norm();

    public static Norm getNorm() {
        return norm;
    }

    public static void setNorm(Norm norm) {
        Args.notNull(norm,"default norm");
        Norms.norm = norm;
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

    public static Properties getInfo() {
        return getConfiguration().getInfo();
    }

    public static void setInfo(Properties info) {
        getConfiguration().setInfo(info);
    }

    public static DataSource getDataSource() {
        return getConfiguration().getDataSource();
    }

    public static void setDataSource(DataSource dataSource) {
        getConfiguration().setDataSource(dataSource);
    }

    public static String getDriverClass() {
        return getConfiguration().getDriverClass();
    }

    public static void setDriverClass(String driverClass) {
        getConfiguration().setDriverClass(driverClass);
    }

    public static int getMaxRecursion() {
        return getConfiguration().getMaxRecursion();
    }

    public static void setMaxRecursion(int maxRecursion) {
        getConfiguration().setMaxRecursion(maxRecursion);
    }

    public static String getUrl() {
        return getConfiguration().getUrl();
    }

    public static void setUrl(String url) {
        getConfiguration().setUrl(url);
    }

    public static String getUsername() {
        return getConfiguration().getUsername();
    }

    public static void setUsername(String username) {
        getConfiguration().setUsername(username);
    }

    public static String getPassword() {
        return getConfiguration().getPassword();
    }

    public static void setPassword(String password) {
        getConfiguration().setPassword(password);
    }


    public static Connection getConnection() throws SQLException {
        return getConfiguration().getConnection();
    }

    public static void registerDriver() throws ClassNotFoundException {
        getConfiguration().registerDriver();
    }

    public static SQLLogger getSqlLogger() {
        return getConfiguration().getSqlLogger();
    }

    public static void setSqlLogger(SQLLogger sqlLogger) {
        getConfiguration().setSqlLogger(sqlLogger);
    }

    public static TableNameStrategy getTableNameStrategy() {
        return getConfiguration().getTableNameStrategy();
    }

    public static void setTableNameStrategy(TableNameStrategy tableNameStrategy) {
        getConfiguration().setTableNameStrategy(tableNameStrategy);
    }


    public static CacheManager getCacheManager() {
        return getConfiguration().getCacheManager();
    }

    public static void setCacheManager(CacheManager cacheManager) {
        getConfiguration().setCacheManager(cacheManager);
    }

    public static boolean isFormatSql() {
        return getConfiguration().isFormatSql();
    }

    public static void setFormatSql(boolean formatSql) {
        getConfiguration().setFormatSql(formatSql);
    }

    public static boolean isShowSql() {
        return getConfiguration().isShowSql();
    }

    public static void setShowSql(boolean showSql) {
        getConfiguration().setShowSql(showSql);
    }

    public static boolean isDriverRegistered() {
        return getConfiguration().isDriverRegistered();
    }

    public static SQLFormatter getSqlFormatter() {
        return getConfiguration().getSqlFormatter();
    }

    public static void setSqlFormatter(SQLFormatter sqlFormatter) {
        getConfiguration().setSqlFormatter(sqlFormatter);
    }
}
