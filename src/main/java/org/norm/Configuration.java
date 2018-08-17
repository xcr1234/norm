package org.norm;

import org.norm.meta.MetaFactory;
import org.norm.naming.NameStrategy;

public class Configuration {
    private String schema;
    private NameStrategy tableNameStrategy;
    private NameStrategy columnNameStrategy;
    private MetaFactory metaFactory;
    private int nullJdbcType;


    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public NameStrategy getTableNameStrategy() {
        return tableNameStrategy;
    }

    public void setTableNameStrategy(NameStrategy tableNameStrategy) {
        this.tableNameStrategy = tableNameStrategy;
    }

    public NameStrategy getColumnNameStrategy() {
        return columnNameStrategy;
    }

    public void setColumnNameStrategy(NameStrategy columnNameStrategy) {
        this.columnNameStrategy = columnNameStrategy;
    }

    public MetaFactory getMetaFactory() {
        return metaFactory;
    }

    public void setMetaFactory(MetaFactory metaFactory) {
        this.metaFactory = metaFactory;
    }

    public int getNullJdbcType() {
        return nullJdbcType;
    }

    public void setNullJdbcType(int nullJdbcType) {
        this.nullJdbcType = nullJdbcType;
    }
}
