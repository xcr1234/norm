package org.norm.core.log;

public class SysOutLogger implements SqlLogger {
    @Override
    public void log(String sql) {
        System.out.println(sql);
    }
}
