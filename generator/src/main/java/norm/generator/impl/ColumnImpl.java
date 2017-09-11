package norm.generator.impl;

import norm.generator.Column;
import norm.generator.Converter;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ColumnImpl extends AbsColumn implements Column {




    private int sqlType;
    private String sqlTypeName;
    private String column;
    private String javaName;
    private Class javaType;
    private int columnIndex;

    public ColumnImpl(ResultSetMetaData metaData, int columnIndex, Converter converter) throws SQLException {
        this.sqlType = metaData.getColumnType(columnIndex);
        this.sqlTypeName = metaData.getColumnTypeName(columnIndex);
        this.column = metaData.getColumnName(columnIndex);
        this.javaName = converter.getJavaName(column,this,null);
        this.javaType = converter.getJavaType(sqlType,sqlTypeName,this);
        this.columnIndex = columnIndex;
    }

    ColumnImpl(ResultSetMetaData metaData, int columnIndex, Converter converter,Column c) throws SQLException{
        this.sqlType = metaData.getColumnType(columnIndex);
        this.sqlTypeName = metaData.getColumnTypeName(columnIndex);
        this.column = metaData.getColumnName(columnIndex);
        this.javaName = converter.getJavaName(column,c,null);
        this.javaType = converter.getJavaType(sqlType,sqlTypeName,c);
        this.columnIndex = columnIndex;
    }

    @Override
    public Class getJavaTypeClass() {
        return this.javaType;
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

}
