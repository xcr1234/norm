package org.norm.core.log;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonsLogger implements SqlLogger{

    private static Log logger = LogFactory.getLog(CommonsLogger.class);

    private DefaultSqlLogger.Level level;

    public DefaultSqlLogger.Level getLevel() {
        return level;
    }

    public void setLevel(DefaultSqlLogger.Level level) {
        this.level = level;
    }


    public CommonsLogger(DefaultSqlLogger.Level level) {
        this.level = level;
    }

    @Override
    public void log(String sql) {
        switch (level){
            case ERROR:
                if(logger.isErrorEnabled()){
                    logger.error(sql);
                }
                break;
            case INFO:
                if(logger.isInfoEnabled()){
                    logger.info(sql);
                }
                break;
            case WARN:
                if(logger.isWarnEnabled()){
                    logger.warn(sql);
                }
                break;
            case DEBUG:
                if(logger.isDebugEnabled()){
                    logger.debug(sql);
                }
                break;
            case FATAL:
                if(logger.isTraceEnabled()){
                    logger.fatal(sql);
                }
                break;
            case TRACE:
                if(logger.isTraceEnabled()){
                    logger.trace(sql);
                }
                break;
        }
    }
}
