package norm;


import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import norm.cache.CacheManager;
import norm.impl.DaoInterceptor;
import norm.impl.Meta;
import norm.impl.MethodFilter;
import norm.impl.ObjectServiceInterceptor;
import norm.impl.ServiceInterceptor;
import norm.naming.TableNameStrategy;
import norm.util.Args;
import norm.util.BeanUtils;


import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Norm框架的核心类
 */
public final class Norm implements Serializable{

    private static final long serialVersionUID = -4345155301336226577L;
    private Configuration configuration;
    private ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();
    private Transactional transactional = new Transactional(this);


    public Norm() {
        this.configuration = new Configuration();
    }

    public Norm(Norm copy) {
        this.configuration = new Configuration(copy.configuration);
    }

    public Norm(Configuration configuration){
        if(configuration == null){
            throw new NullPointerException();
        }
        this.configuration = configuration;
    }

    public Connection getConnection() {
        if (!configuration.isDriverRegistered()) {
            try {
                configuration.registerDriver();
            } catch (ClassNotFoundException e) {
                throw new QueryException("can't register driver:" + configuration.getDriverClass(), e);
            }
        }
        Connection connection = connectionThreadLocal.get();
        if (connection == null || isClosed(connection)) {
            try {
                connection = configuration.getConnection();
                connectionThreadLocal.set(connection);
            } catch (SQLException e) {
                throw new QueryException(e);
            }
        }
        return connection;
    }

    public void registerDriver() throws ClassNotFoundException {
        configuration.registerDriver();
    }

    public SQLLogger getSqlLogger() {
        return configuration.getSqlLogger();
    }

    public void setSqlLogger(SQLLogger sqlLogger) {
        configuration.setSqlLogger(sqlLogger);
    }

    public void setConfiguration(Configuration configuration) {
        Args.notNull(configuration,"configuration");
        this.configuration = configuration;
    }

    public Transactional getTransactional() {
        return transactional;
    }

    public void closeConnection(){
        Connection connection = connectionThreadLocal.get();
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {}
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    private Map<Class,Object> daoCache = new ConcurrentHashMap<Class,Object>();
    private Map<Class,Object> serviceCache = new ConcurrentHashMap<Class,Object>();

    @SuppressWarnings("unchecked")
    private <Dao extends CrudDao<T,I>,T,I> Dao createDao(Class<Dao> daoClass, Class<T> tClass, Class<I> idClass){
        if(Modifier.isFinal(tClass.getModifiers()) || Modifier.isAbstract(tClass.getModifiers())){
            throw new BeanException("entity class can't be final or abstract :"+tClass);
        }
        Meta meta = Meta.parse(tClass,getTableNameStrategy());
        if(meta.getIdColumn().getType() != idClass){
            throw new BeanException("illegal bean,id type mismatch :" + tClass +",dao:"+daoClass);
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{daoClass});
        enhancer.setCallbacks(new Callback[]{new DaoInterceptor(tClass,daoClass,this), NoOp.INSTANCE});
        enhancer.setCallbackFilter(MethodFilter.getInstance());
        return (Dao) enhancer.create();
    }




    @SuppressWarnings("unchecked")
    public <Dao extends CrudDao<T,I>,T,I> Dao createDao(Class<Dao> daoClass){
        Args.notNull(daoClass,"daoClass");
        if(!daoClass.isInterface()){
            throw new IllegalArgumentException("dao class : '" + daoClass + "' should be an interface.");
        }
        if(!CrudDao.class.isAssignableFrom(daoClass)){
            throw new BeanException("illegal dao :"+daoClass+",dao interface class should extends CrudDao<T,ID>");
        }
        Type[] types = daoClass.getGenericInterfaces();
        for(Type t:types){
            if(t instanceof ParameterizedType){
                ParameterizedType pt = (ParameterizedType) t;
                Type[] arguments = pt.getActualTypeArguments();
                if(arguments.length == 2){
                    Type t0 = arguments[0] , t1 = arguments[1];
                    if(t0 instanceof Class && t1 instanceof Class && BeanUtils.isSerializable((Class<?>) t1)){
                        Object dao = daoCache.get(daoClass);
                        if(dao == null){
                            try {
                                dao = createDao(daoClass,(Class<T>)t0,(Class<I>)t1);
                                daoCache.put(daoClass,dao);
                            }catch (ClassCastException e){
                                throw new BeanException("illegal dao:"+daoClass);
                            }
                        }
                        return (Dao) dao;
                    }
                }
            }
        }
        throw new BeanException("illegal dao:"+daoClass+",dao interface class should extends CrudDao<T,ID>");


    }

    public Object enableCache(Object object){
        Args.notNull(object,"cache object");
        Class c = object.getClass();
        if(Modifier.isFinal(c.getModifiers())){
            throw new BeanException("illegal service object:class '"+c+" 'is final");
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(c);
        enhancer.setCallbackFilter(MethodFilter.getInstance());
        enhancer.setCallbacks(new Callback[]{new ObjectServiceInterceptor(object,this), NoOp.INSTANCE});
        return enhancer.create();
    }

    @SuppressWarnings("unchecked")
    public <Service> Service createService(Class<Service> serviceClass){
        Args.notNull(serviceClass,"service class");
        if(Modifier.isFinal(serviceClass.getModifiers())){
            throw new BeanException("illegal service:" + serviceClass + ",class is final");
        }
        Object value = serviceCache.get(serviceClass);
        if(value == null){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(serviceClass);
            enhancer.setCallbackFilter(MethodFilter.getInstance());
            enhancer.setCallbacks(new Callback[]{new ServiceInterceptor(serviceClass,this), NoOp.INSTANCE});
            value = enhancer.create();
            serviceCache.put(serviceClass,value);
        }
        return (Service) value;
    }

    public TableNameStrategy getTableNameStrategy() {
        return configuration.getTableNameStrategy();
    }

    public void setTableNameStrategy(TableNameStrategy tableNameStrategy) {
        configuration.setTableNameStrategy(tableNameStrategy);
    }

    public CacheManager getCacheManager() {
        return configuration.getCacheManager();
    }

    public void setCacheManager(CacheManager cacheManager) {
        configuration.setCacheManager(cacheManager);
    }

    public boolean isFormat_sql() {
        return configuration.isFormatSql();
    }

    public void setFormat_sql(boolean format_sql) {
        configuration.setFormatSql(format_sql);
    }

    public boolean isShow_sql() {
        return configuration.isShowSql();
    }

    public void setShow_sql(boolean show_sql) {
        configuration.setShowSql(show_sql);
    }

    public void showSQL(String sql){
        configuration.getSqlLogger().logSQL(sql);
    }

    private static boolean isClosed(Connection connection) {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException{
        out.writeObject(configuration);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
        configuration = (Configuration) in.readObject();
        connectionThreadLocal = new ThreadLocal<Connection>();
        transactional = new Transactional(this);
        daoCache = new ConcurrentHashMap<Class,Object>();
        serviceCache = new ConcurrentHashMap<Class,Object>();
    }

    public Properties getInfo() {
        return configuration.getInfo();
    }

    public Norm setInfo(Properties info) {
        configuration.setInfo(info);
        return this;
    }

    public DataSource getDataSource() {
        return configuration.getDataSource();
    }

    public Norm setDataSource(DataSource dataSource) {
        configuration.setDataSource(dataSource);
        return this;
    }

    public String getDriverClass() {
        return configuration.getDriverClass();
    }

    public Norm setDriverClass(String driverClass) {
        configuration.setDriverClass(driverClass);
        return this;
    }

    public int getMaxRecursion() {
        return configuration.getMaxRecursion();
    }

    public Norm setMaxRecursion(int maxRecursion) {
        configuration.setMaxRecursion(maxRecursion);
        return this;
    }

    public String getUrl() {
        return configuration.getUrl();
    }

    public Norm setUrl(String url) {
        configuration.setUrl(url);
        return this;
    }

    public String getUsername() {
        return configuration.getUsername();
    }

    public Norm setUsername(String username) {
        configuration.setUsername(username);
        return this;
    }

    public String getPassword() {
        return configuration.getPassword();
    }

    public Norm setPassword(String password) {
        configuration.setPassword(password);
        return this;
    }

    public boolean isFormatSql() {
        return configuration.isFormatSql();
    }

    public void setFormatSql(boolean formatSql) {
        configuration.setFormatSql(formatSql);
    }

    public boolean isShowSql() {
        return configuration.isShowSql();
    }

    public void setShowSql(boolean showSql) {
        configuration.setShowSql(showSql);
    }

    public boolean isDriverRegistered() {
        return configuration.isDriverRegistered();
    }

    public SQLFormatter getSqlFormatter() {
        return configuration.getSqlFormatter();
    }

    public void setSqlFormatter(SQLFormatter sqlFormatter) {
        configuration.setSqlFormatter(sqlFormatter);
    }
}
