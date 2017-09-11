package norm.generator.impl;

import norm.generator.Column;
import norm.generator.Converter;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class NameColumnImpl extends AbsColumn implements Column {


    private ColumnImpl column;
    private int columnIndex;

    public NameColumnImpl(Converter converter,String columnName, ResultSet resultSet, ResultSetMetaData metaData) throws SQLException{
        this.columnIndex = resultSet.findColumn(columnName);
        this.column = new ColumnImpl(metaData,columnIndex,converter,this);
    }

    @Override
    public Class getJavaTypeClass() {
        return column.getJavaTypeClass();
    }

    @Override
    public String getJavaType() {
        return column.getJavaType();
    }

    @Override
    public int getSQLType() throws SQLException {
        return column.getSQLType();
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return column.getSQLTypeName();
    }

    @Override
    public String getJavaName() {
        return column.getJavaName();
    }

    @Override
    public String getColumnName() {
        return column.getColumnName();
    }

}
