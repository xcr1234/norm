package norm.generator.impl;

import norm.generator.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseEntityBuilder implements EntityBuilder {
    @Override
    public List<Entity> build(Map<String, String> argMap, Converter converter, String basePackage) throws GenerateException {
        String table = argMap.get("table");
        if(table == null){
            throw new GenerateException("generator run failed,sql is null!");
        }
        String driver = argMap.get("driverClass");
        if(driver == null){
            throw new GenerateException("generator run failed,driverClass is null!");
        }
        try{
            Class.forName(driver);
        }catch (ClassNotFoundException e){
            throw new GenerateException("generator run failed,driverClass not found:" + driver);
        }
        String url = argMap.get("jdbcUrl");
        String user = argMap.get("username");
        String pass = argMap.get("password");

        String catalog = argMap.get("catalog");
        String schema = argMap.get("schema");

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url,user,pass);

            DatabaseMetaData metaData = connection.getMetaData();

            List<Entity> entities = new ArrayList<Entity>();
            ResultSet tableRet = metaData.getTables(catalog,"%",table,new String[]{"TABLE"});
            boolean flag = true;
            while (tableRet.next()){
                entities.add(createEntity(metaData,tableRet,converter,catalog,schema,basePackage));
                flag = false;
            }
            if(flag){
                throw new GenerateException("table not found:" + table);
            }
            return entities;
        }catch (SQLException e){
            throw new GenerateException("database error.",e);
        }finally{
            try{
                if(connection != null){
                    connection.close();
                }
            }catch (SQLException e){}
        }

    }

    private Column createColumn(ResultSet resultSet,Converter converter) throws SQLException{
        String columnName = resultSet.getString("COLUMN_NAME");
        int sqlType = resultSet.getInt("DATA_TYPE");
        String sqlTypeName = resultSet.getString("TYPE_NAME");
        return new DataBaseColumn(columnName,sqlType,sqlTypeName,converter);
    }

    private Entity createEntity(DatabaseMetaData metaData,ResultSet tableRet,Converter converter,String catalog,String schema,String basePackage) throws SQLException, GenerateException {
        String tableName = tableRet.getString("TABLE_NAME");

        ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(catalog,schema,tableName);
        if(!primaryKeyResultSet.next()){
            throw new GenerateException("table has no primary key:" + tableName);
        }
        String idName = primaryKeyResultSet.getString("COLUMN_NAME");

        if(primaryKeyResultSet.next()){
            throw new GenerateException("table has multiple primary keys:" + tableName);
        }

        Column idColumn = null;
        List<Column> columns = new ArrayList<Column>();
        ResultSet colRet = metaData.getColumns(catalog,schema,tableName,"%");
        while (colRet.next()){
            Column column = createColumn(colRet,converter);
            if(column.getColumnName().equals(idName)){
                idColumn = column;
            }else{
                columns.add(column);
            }
        }
        if(idColumn == null){
            throw new GenerateException("table has no primary key:" + tableName);
        }
        return new DatabaseEntity(tableName,converter,idColumn,columns,basePackage);
    }

    @Override
    public String getMode() {
        return "database";
    }
}
