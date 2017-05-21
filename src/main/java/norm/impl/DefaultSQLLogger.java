package norm.impl;


import norm.SQLLogger;

public final class DefaultSQLLogger implements SQLLogger {

    public static final DefaultSQLLogger DEFAULT = new DefaultSQLLogger();

    @Override
    public void logSQL(String sql) {
        System.out.println(sql);
    }
}
