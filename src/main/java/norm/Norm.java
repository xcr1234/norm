package norm;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import norm.anno.FieldStrategy;
import norm.convert.DefaultEnumConverter;
import norm.convert.EnumConverter;
import norm.core.executor.DefaultExecutorFactory;
import norm.core.executor.ExecutorFactory;
import norm.core.generator.DefaultGeneratorFactory;
import norm.core.generator.QueryGenerator;
import norm.core.generator.QueryGeneratorFactory;
import norm.core.id.DefaultIdGenerator;
import norm.core.id.IdGenerator;
import norm.core.interceptor.CrudProxyImpl;
import norm.core.interceptor.CrudDaoInterceptor;
import norm.core.interceptor.CrudProxy;
import norm.core.log.DefaultSqlLogger;
import norm.core.log.SqlLogger;
import norm.core.meta.Meta;
import norm.exception.DefaultExceptionTranslator;
import norm.exception.ExceptionTranslator;
import norm.exception.ExecutorException;
import norm.exception.SpringExceptionTranslator;
import norm.naming.DefaultTableNameStrategy;
import norm.naming.NameStrategy;
import norm.page.PageSql;
import norm.page.PageUtil;
import norm.page.impl.*;
import norm.util.*;
import norm.util.sql.BasicFormatterImpl;

import javax.sql.DataSource;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Norm {
    private boolean driverFlag;
    private boolean showSql;
    private boolean formatSql;
    private PageSql pageSql;
    private boolean getGenerateId = true;
    private SqlLogger sqlLogger = new DefaultSqlLogger();
    private SQLFormatter sqlFormatter = new BasicFormatterImpl();
    private ExecutorFactory executorFactory = new DefaultExecutorFactory();
    private NameStrategy tableNameStrategy = new DefaultTableNameStrategy();
    private NameStrategy columnNameStrategy = new DefaultTableNameStrategy();
    private IdGenerator idGenerator = new DefaultIdGenerator();
    private EnumConverter enumConverter = new DefaultEnumConverter();
    private String schema;
    private int jdbcNullType = Types.OTHER;
    private FieldStrategy insertStrategy;
    private FieldStrategy updateStrategy;

    //connection configurations:
    private DataSource dataSource;
    private String driverClass;
    private String url;
    private String username;
    private String password;
    private Properties connProperties;
    private ExceptionTranslator exceptionTranslator = new DefaultExceptionTranslator();
    private QueryGeneratorFactory generatorFactory = new DefaultGeneratorFactory(this);
    private Map<Class, Object> daoCache = new HashMap<Class, Object>();
    private ThreadLocal<TransactionManager> managerThreadLocal = new ThreadLocal<TransactionManager>() {
        @Override
        protected TransactionManager initialValue() {
            return new TransactionManager();
        }
    };
    private Map<Class,Meta> metaMap = Collections.synchronizedMap(new HashMap<Class,Meta>());

    public Norm(){

    }

    public ExceptionTranslator getExceptionTranslator(){
        return this.exceptionTranslator;
    }

    public Meta getMeta(Class type){
        Meta meta = metaMap.get(type);
        if(meta == null){
            meta = new Meta(type,this);
            metaMap.put(type,meta);
        }
        return meta;
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

    public PageSql getPageSql() {
        return pageSql;
    }

    public void setPageSql(PageSql pageSql) {
        this.pageSql = pageSql;
    }

    public boolean isGetGenerateId() {
        return getGenerateId;
    }

    public void setGetGenerateId(boolean getGenerateId) {
        this.getGenerateId = getGenerateId;
    }

    public SqlLogger getSqlLogger() {
        return sqlLogger;
    }

    public void setSqlLogger(SqlLogger sqlLogger) {
        this.sqlLogger = sqlLogger;
    }

    public SQLFormatter getSqlFormatter() {
        return sqlFormatter;
    }

    public void setSqlFormatter(SQLFormatter sqlFormatter) {
        this.sqlFormatter = sqlFormatter;
    }

    public ExecutorFactory getExecutorFactory() {
        return executorFactory;
    }

    public void setExecutorFactory(ExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

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

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
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

    public FieldStrategy getInsertStrategy() {
        return insertStrategy;
    }

    public void setInsertStrategy(FieldStrategy insertStrategy) {
        this.insertStrategy = insertStrategy;
    }

    public FieldStrategy getUpdateStrategy() {
        return updateStrategy;
    }

    public void setUpdateStrategy(FieldStrategy updateStrategy) {
        this.updateStrategy = updateStrategy;
    }

    public EnumConverter getEnumConverter() {
        return enumConverter;
    }

    public void setEnumConverter(EnumConverter enumConverter) {
        this.enumConverter = enumConverter;
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
        this.driverClass = driverClass;
        this.driverFlag = false;
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

    public Properties getConnProperties() {
        return connProperties;
    }

    public void setConnProperties(Properties connProperties) {
        this.connProperties = connProperties;
    }

    public QueryGeneratorFactory getGeneratorFactory() {
        return generatorFactory;
    }

    public void setGeneratorFactory(QueryGeneratorFactory generatorFactory) {
        this.generatorFactory = generatorFactory;
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
            throw new ExecutorException("the jdbc url or dataSource of norm not configured!");
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

    /**
     * get current connection , never be null / closed
     * @return the current connection
     * @throws SQLException throws when create connection error.
     */
    public Connection openConnection() throws SQLException {
        TransactionManager manager = managerThreadLocal.get();
        Connection connection = manager.getConnection();
        if(JdbcUtils.connClosed(connection)){
            ErrorContext.instance().setState("open connection");
            connection = newConnection();
            manager.setConnection(connection);
            if(manager.isBegin()){
                connection.setAutoCommit(false);
            }
        }
        if(getPageSql() == null && !trySetPageSql){
            trySetPageSql = true;
            trySetPageSql(connection);
        }
        return connection;
    }

    protected boolean trySetPageSql = false;

    protected void trySetPageSql(Connection connection) throws SQLException{
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String databaseProductName = databaseMetaData.getDatabaseProductName();
        if(databaseProductName != null){
            PageSql pageSql = PageUtil.getPageSql(databaseProductName);
            if(pageSql != null){
                setPageSql(pageSql);
            }
        }
    }

    public RuntimeException handleError(Exception e) {
        TransactionManager transactionManager = managerThreadLocal.get();
        Connection connection = transactionManager.getConnection();
        if (!JdbcUtils.connClosed(connection) && transactionManager.isBegin()) {
            transactionManager.setBegin(false);
            try {
                connection.rollback();
                return getExceptionTranslator().translate("rollback success.", e);
            } catch (SQLException e1) {
                return getExceptionTranslator().translate("rollback failed :" + e1, e);
            }
        }
        return getExceptionTranslator().translate(null, e);
    }


    public void releaseConnection(Connection connection) {
        TransactionManager manager = managerThreadLocal.get();
        if(!manager.isBegin()){
            JdbcUtils.close(connection);
            manager.setConnection(null);
        }
    }

    public boolean isBegin(){
        return managerThreadLocal.get().isBegin();
    }

    public void begin() {
        TransactionManager manager = managerThreadLocal.get();
        manager.setBegin(true);
    }

    public void close(){
        TransactionManager manager = managerThreadLocal.get();
        manager.setConnection(null);
        manager.setBegin(false);
        Connection connection = manager.getConnection();
        JdbcUtils.close(connection);
    }



    public void commit(){
        TransactionManager transactionManager = managerThreadLocal.get();
        Connection connection = transactionManager.getConnection();
        transactionManager.setBegin(false);
        if(!JdbcUtils.connClosed(connection)){
            ErrorContext.instance().setState("commit transaction");
            try {
                connection.commit();
            } catch (SQLException e) {
                throw handleError(e);
            }finally {
                releaseConnection(connection);
            }
        }
    }

    public void rollback() {
        TransactionManager transactionManager = managerThreadLocal.get();
        transactionManager.setBegin(false);
        Connection connection = transactionManager.getConnection();
        if(!JdbcUtils.connClosed(connection)){
            ErrorContext.instance().setState("rollback transaction");
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw getExceptionTranslator().translate(null, e);
            }
        }

    }

    /**
     * get the current connection,may be null or closed.
     * @return the current connection
     */
    public Connection getCurrentConnection(){
        return getCurrentTransactionManager().getConnection();
    }

    public TransactionManager getCurrentTransactionManager(){
        return managerThreadLocal.get();
    }

    @SuppressWarnings("unchecked")
    public <Dao extends CrudDao<T, I>, T, I> Dao createDao(Class<Dao> daoClass) {
        Args.notNull(daoClass, "daoClass");
        if (!daoClass.isInterface()) {
            throw new IllegalArgumentException("dao class : '" + daoClass + "' should be an interface.");
        }
        if (!CrudDao.class.isAssignableFrom(daoClass)) {
            throw new IllegalArgumentException("illegal dao :" + daoClass + ",dao interface class should extends CrudDao<T,ID>");
        }
        Type[] types = daoClass.getGenericInterfaces();
        for (Type t : types) {
            if (t instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) t;
                Type[] arguments = pt.getActualTypeArguments();
                if (arguments.length == 2) {
                    Type t0 = arguments[0], t1 = arguments[1];
                    if (t0 instanceof Class && t1 instanceof Class && BeanUtils.isSerializable((Class<?>) t1)) {
                        Object dao = daoCache.get(daoClass);
                        if (dao == null) {
                            try {
                                dao = createDao(daoClass, (Class<T>) t0, (Class<I>) t1);
                                daoCache.put(daoClass, dao);
                            } catch (ClassCastException e) {
                                throw new IllegalArgumentException("illegal dao:" + daoClass);
                            }
                        }
                        return (Dao) dao;
                    }
                }
            }
        }
        throw new IllegalArgumentException("illegal dao:" + daoClass + ",dao interface class should extends CrudDao<T,ID>");

    }

    @SuppressWarnings("unchecked")
    private <Dao extends CrudDao<T, I>, T, I> Dao createDao(Class<Dao> daoClass, Class<T> tClass, Class<I> idClass) {
        if (Modifier.isFinal(tClass.getModifiers()) || Modifier.isAbstract(tClass.getModifiers())) {
            throw new IllegalArgumentException("entity class can't be final or abstract :" + tClass);
        }
        Meta meta = Meta.parse(tClass, this);
        if (meta.getIdColumn().getType() != idClass) {
            throw new IllegalArgumentException("illegal bean,id type mismatch :" + tClass + ",dao:" + daoClass);
        }
        CrudProxy crudDao = createCrudProxy(generatorFactory.getGenerator(tClass));
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{daoClass,NormAware.class});
        enhancer.setCallbacks(new Callback[]{new CrudDaoInterceptor(crudDao), NoOp.INSTANCE});
        enhancer.setCallbackFilter(MethodFilter.getInstance());
        return (Dao) enhancer.create();
    }

    public CrudProxy createProxyForDaoType(Class daoType){
        Type [] types = daoType.getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Class beanClass = (Class) parameterizedType.getActualTypeArguments()[0];
        return createCrudProxy(generatorFactory.getGenerator(beanClass));
    }

    protected CrudProxy createCrudProxy(QueryGenerator generator){
        return new CrudProxyImpl(this, generator);
    }


}
