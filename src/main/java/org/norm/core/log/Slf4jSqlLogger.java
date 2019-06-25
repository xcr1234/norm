package org.norm.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jSqlLogger implements SqlLogger {

    private static Logger logger = LoggerFactory.getLogger(Slf4jSqlLogger.class);
    private DefaultSqlLogger.Level level;

    public Slf4jSqlLogger(){
        this(DefaultSqlLogger.getDefaultLevel());
    }

    public Slf4jSqlLogger(DefaultSqlLogger.Level level) {
        this.level = level;
    }

    public DefaultSqlLogger.Level getLevel() {
        return level;
    }

    public void setLevel(DefaultSqlLogger.Level level) {
        this.level = level;
    }

    @Override
    public void log(String sql) {
        sql = sql.replaceAll("\n"," ").replaceAll("\t"," ");
        switch (level){
            case ERROR:case FATAL: //slf4j no fatal level.
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
            case TRACE:
                if(logger.isTraceEnabled()){
                    logger.trace(sql);
                }
                break;
        }
    }
}
