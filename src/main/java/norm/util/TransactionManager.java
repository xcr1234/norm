package norm.util;

import java.sql.Connection;

public class TransactionManager {

    private Connection connection;
    private boolean begin; //事务是否已经开始


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isBegin() {
        return begin;
    }

    public void setBegin(boolean begin) {
        this.begin = begin;
    }
}
