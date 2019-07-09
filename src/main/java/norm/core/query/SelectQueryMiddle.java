package norm.core.query;

import norm.util.sql.SQL;

/**
 * SelectQuery holds sqlBuilder
 * @param <T>
 */
public class SelectQueryMiddle<T> extends SelectQuery<T> {
    private SQL sqlBuilder;
    private String sql;

    public SelectQueryMiddle(SQL sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    public SQL getSqlBuilder() {
        return sqlBuilder;
    }

    public void setSqlBuilder(SQL sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    @Override
    public String getSql() {
        if(sql == null){
            sql = sqlBuilder.toString();
        }
        return sql;
    }

    @Override
    public void setSql(String sql) {
        this.sql = sql;
    }
}
