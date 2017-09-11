package norm.generator.impl;

import norm.generator.Converter;

import java.sql.SQLException;

public class DataBaseColumn extends AbsColumn {


    private String columnName;
    private int sqlType;
    private String sqlTypeName;
    private Converter converter;

    public DataBaseColumn(String columnName, int sqlType, String sqlTypeName, Converter converter) {
        this.columnName = columnName;
        this.sqlType = sqlType;
        this.sqlTypeName = sqlTypeName;
        this.converter = converter;
    }

    @Override
    public Class getJavaTypeClass() {
        return converter.getJavaType(sqlType,sqlTypeName,this);
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
        return converter.getJavaName(columnName,this,null);
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

}
