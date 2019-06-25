package org.norm.core.log;


import org.norm.util.ReflectUtils;

public class DefaultSqlLogger implements SqlLogger{


    private SqlLogger sqlLogger;
    private static Level defaultLevel = Level.INFO;

    private static final String COMMONS_CLASSNAME = "org.apache.commons.logging.LogFactory";
    private static final String SLF4J_CLASSNAME = "org.slf4j.LoggerFactory";

    public DefaultSqlLogger() {
        if(ReflectUtils.inClasspath(COMMONS_CLASSNAME)){
            sqlLogger = new CommonsSqlLogger(defaultLevel);
        }else if(ReflectUtils.inClasspath(SLF4J_CLASSNAME)){
            sqlLogger = new Slf4jSqlLogger(defaultLevel);
        }else{
            sqlLogger = new SysOutLogger();
        }
    }

    public enum Level{
        DEBUG,ERROR,FATAL,INFO,TRACE,WARN;
    }

    public static Level getDefaultLevel() {
        return defaultLevel;
    }

    public static void setDefaultLevel(Level defaultLevel) {
        DefaultSqlLogger.defaultLevel = defaultLevel;
    }

    public Level getLevel() {
       if(sqlLogger instanceof CommonsSqlLogger){
           return ((CommonsSqlLogger) sqlLogger).getLevel();
       }else if(sqlLogger instanceof Slf4jSqlLogger){
           return ((Slf4jSqlLogger) sqlLogger).getLevel();
       }else{
           throw new IllegalStateException("commons logging/slf4j not in classpath.");
       }
    }

    public void setLevel(Level level) {
        if(sqlLogger instanceof CommonsSqlLogger){
            ((CommonsSqlLogger) sqlLogger).setLevel(level);
        }else if(sqlLogger instanceof Slf4jSqlLogger){
            ((Slf4jSqlLogger) sqlLogger).setLevel(level);
        }else{
            throw new IllegalStateException("commons logging/slf4j not in classpath.");
        }
    }



    @Override
    public void log(String sql) {
        sqlLogger.log(sql);
    }
}
