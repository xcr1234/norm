package norm.generator.impl;

import norm.generator.Column;
import norm.generator.Converter;
import norm.generator.Entity;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseEntity extends AbsEntity implements Entity {


    private String tableName;
    private Converter converter;
    private Column idColumn;
    private List<Column> columns;
    private String basePackage;

    public DatabaseEntity(String tableName, Converter converter, Column idColumn, List<Column> columns, String basePackage) {
        this.tableName = tableName;
        this.converter = converter;
        this.idColumn = idColumn;
        this.columns = columns;
        this.basePackage = basePackage;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getName() {
        return converter.getJavaName(tableName,null,this);
    }

    @Override
    public Column getIdColumn() {
        return idColumn;
    }

    @Override
    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public String getBasePackage() {
        return basePackage;
    }
}
