package org.norm.core.log;


import org.norm.util.ReflectUtils;

public class DefaultSqlLogger implements SqlLogger{


    private SqlLogger sqlLogger;
    private static Level defaultLevel = Level.INFO;

    private static final String LOG4J_CLASSNAME = "org.apache.commons.logging.LogFactory";
    private static final String SLF4J_CLASSNAME = "org.slf4j.LoggerFactory";

    public DefaultSqlLogger() {
        if(ReflectUtils.inClasspath(LOG4J_CLASSNAME)){
            sqlLogger = new CommonsLogger(defaultLevel);
        }else if(ReflectUtils.inClasspath(SLF4J_CLASSNAME)){
            sqlLogger = new Slf4jLogger(defaultLevel);
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
       if(sqlLogger instanceof CommonsLogger){
           return ((CommonsLogger) sqlLogger).getLevel();
       }else if(sqlLogger instanceof Slf4jLogger){
           return ((Slf4jLogger) sqlLogger).getLevel();
       }else{
           throw new IllegalStateException("commons logging/slf4j not in classpath.");
       }
    }

    public void setLevel(Level level) {
        if(sqlLogger instanceof CommonsLogger){
            ((CommonsLogger) sqlLogger).setLevel(level);
        }else if(sqlLogger instanceof Slf4jLogger){
            ((Slf4jLogger) sqlLogger).setLevel(level);
        }else{
            throw new IllegalStateException("commons logging/slf4j not in classpath.");
        }
    }



    @Override
    public void log(String sql) {
        sqlLogger.log(sql);
    }
}
