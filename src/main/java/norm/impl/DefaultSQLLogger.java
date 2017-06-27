package norm.impl;


import norm.SQLLogger;

public final class DefaultSQLLogger implements SQLLogger {

    private static final DefaultSQLLogger DEFAULT = new DefaultSQLLogger();

    public static DefaultSQLLogger getInstance(){
        return DEFAULT;
    }

    @Override
    public void logSQL(String sql) {
        System.out.println(sql);
    }
}
