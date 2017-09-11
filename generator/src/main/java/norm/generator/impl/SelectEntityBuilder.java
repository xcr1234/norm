package norm.generator.impl;

import norm.generator.Converter;
import norm.generator.Entity;
import norm.generator.EntityBuilder;
import norm.generator.GenerateException;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SelectEntityBuilder  implements EntityBuilder {
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
        String idColumn = argMap.get("idColumn");
        if(idColumn == null){
            throw new GenerateException("generator run failed,idColumn is null!");
        }
        String url = argMap.get("jdbcUrl");
        String user = argMap.get("username");
        String pass = argMap.get("password");
        Connection connection = null;
        try{
            String sql = "SELECT * from " + table;
            connection = DriverManager.getConnection(url,user,pass);
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            Entity entity =  new ResultSetEntity(resultSet,converter,basePackage,table,idColumn);
            return Collections.singletonList(entity);
        }catch (SQLException e){
           throw new GenerateException("database error.",e);
        } finally{
            try{
                if(connection != null){
                    connection.close();
                }
            }catch (SQLException e){}
        }
    }

    @Override
    public String getMode() {
        return "select";
    }
}
