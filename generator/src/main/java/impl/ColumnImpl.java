package impl;

import norm.generator.Column;
import norm.generator.Converter;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ColumnImpl implements Column {


    private ResultSetMetaData metaData;
    private int columnIndex;
    private Converter converter;

    private int sqlType;
    private String sqlTypeName;
    private String column;
    private String javaName;
    private Class javaType;

    public ColumnImpl(ResultSetMetaData metaData, int columnIndex, Converter converter) throws SQLException {
        this.metaData = metaData;
        this.columnIndex = columnIndex;
        this.converter = converter;

        this.sqlType = metaData.getColumnType(columnIndex);
        this.sqlTypeName = metaData.getColumnTypeName(columnIndex);
        this.column = metaData.getColumnName(columnIndex);
        this.javaName = converter.getJavaName(column);
        this.javaType = converter.getJavaType(sqlType,sqlTypeName);
    }

    @Override
    public Class getJavaType() throws SQLException {
        return javaType;
    }

    @Override
    public int getSQLType() throws SQLException {
        return sqlType;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return sqlTypeName;
    }

    @Override
    public String getJavaName() {
        return javaName;
    }

    @Override
    public String getColumnName() {
        return column;
    }

    @Override
    public String getGetterName() {
        if(javaType == boolean.class || javaType == Boolean.class){
            return "is" + StringUtils.capitalize(this.javaName);
        }
        return "get" + StringUtils.capitalize(this.javaName);
    }

    @Override
    public String getSetterName() {
        return "set" + StringUtils.capitalize(this.javaName);
    }

}
