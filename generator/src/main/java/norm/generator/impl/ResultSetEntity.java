package norm.generator.impl;

import norm.generator.Column;
import norm.generator.Converter;
import norm.generator.Entity;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetEntity extends AbsEntity implements Entity {

    private String basePackage;
    private Column idColumn;
    private List<Column> columns = new ArrayList<Column>();
    private String table;
    private Converter converter;
    public ResultSetEntity(ResultSet resultSet, Converter converter,String basePackage,String table,String idName)
            throws SQLException{
        this.basePackage = basePackage;
        this.converter = converter;
        ResultSetMetaData metaData = resultSet.getMetaData();
        this.idColumn = new NameColumnImpl(converter,idName,resultSet,metaData);
        for(int i=1;i<=metaData.getColumnCount();i++){
            if(!metaData.getColumnName(i).equals(idName)){
                columns.add(new ColumnImpl(metaData,i,converter));
            }
        }
        this.table = table;
    }


    @Override
    public String getTableName() {
        return this.table;
    }

    @Override
    public String getName() {
        return converter.getJavaName(this.table,null,this);
    }

    @Override
    public Column getIdColumn() {
        return this.idColumn;
    }

    @Override
    public List<Column> getColumns() {
        return this.columns;
    }

    @Override
    public String getBasePackage() {
        return basePackage;
    }
}
