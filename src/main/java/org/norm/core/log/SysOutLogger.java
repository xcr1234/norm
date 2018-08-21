package org.norm.core.log;

public class SysOutLogger implements SqlLogger {
    @Override
    public void log(String sql) {
        System.out.println("[Norm SQL Logger] " + sql.replaceAll("\n"," ").replaceAll("\t"," "));
    }
}
