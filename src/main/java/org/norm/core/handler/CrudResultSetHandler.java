package org.norm.core.handler;

import org.norm.TypeConverter;
import org.norm.core.meta.ColumnMeta;
import org.norm.core.meta.Meta;
import org.norm.util.JdbcUtils;
import org.norm.util.ReflectUtils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class CrudResultSetHandler implements ResultSetHandler {

    private Meta meta;

    public CrudResultSetHandler(Meta meta) {
        this.meta = meta;
    }

    @Override
    public Object handle(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        Set<String> columns = new HashSet<String>();
        int count = metaData.getColumnCount();
        for(int i=1;i<=count;i++){
            columns.add(metaData.getColumnLabel(i));
        }

        Object object = ReflectUtils.newInstance(meta.getClazz());

        for(ColumnMeta columnMeta : meta.getColumnMetas().values()){
            if(columnMeta.select()){
                boolean setFlag = false;
                Object value = null;
                String columnName = columnMeta.getColumnName();
                TypeConverter<?> converter = columnMeta.getTypeConverter();
                if(columns.contains(columnName)){
                    setFlag = true;
                    if(converter != null){
                        value = converter.getObject(resultSet,columnName);
                    }else{
                        value = JdbcUtils.getObject(resultSet,columnName,columnMeta.getType());
                    }
                }else if(columns.contains(columnName.toUpperCase())){
                    setFlag = true;
                    if(converter != null){
                        value = converter.getObject(resultSet,columnName.toUpperCase());
                    }else{
                        value = JdbcUtils.getObject(resultSet,columnName.toUpperCase(),columnMeta.getType());
                    }
                }
                if(setFlag){
                    columnMeta.set(object,value);
                }
            }
        }

        for(Method method:meta.getAfterInstanceMethods()){
            ReflectUtils.invoke(method,object);
        }

        return object;
    }
}
