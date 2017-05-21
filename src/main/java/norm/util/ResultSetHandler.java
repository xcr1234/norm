package norm.util;

import norm.QueryException;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;


public final class ResultSetHandler {



    //注意，两个函数虽然代码一样，但是一个是int型，一个是string型。
    public static Object get(ResultSet resultSet, String index, Class type,String columnMeta) throws SQLException {
        if(type == String.class){
            return resultSet.getString(index);
        }else if(type == Boolean.class || type == boolean.class){
            return resultSet.getBoolean(index);
        }else if(type == Byte.class || type == byte.class){
            return resultSet.getByte(index);
        }else if(type == Short.class || type == short.class){
            return resultSet.getShort(index);
        }else if(type == Integer.class || type == int.class){
            return resultSet.getInt(index);
        }else if(type == Long.class || type == long.class){
            return resultSet.getLong(index);
        }else if(type == Float.class || type == float.class){
            return resultSet.getFloat(index);
        }else if(type == Double.class || type == double.class){
            return resultSet.getDouble(index);
        }else if(type == BigDecimal.class){
            return resultSet.getBigDecimal(index);
        }else if(type == byte[].class){
            return resultSet.getBytes(index);
        }else if(type == java.util.Date.class){ //这里需要转化一下
            long time = resultSet.getDate(index).getTime();
            return new java.util.Date(time);
        }else if(type == java.sql.Date.class){
            return resultSet.getDate(index);
        }else if(type == java.sql.Time.class){
            return resultSet.getTime(index);
        }else if(type == java.sql.Timestamp.class){
            return resultSet.getTimestamp(index);
        }else if(type == Blob.class){
            return resultSet.getBlob(index);
        }else if(type == Clob.class){
            return resultSet.getClob(index);
        }else if(InputStream.class.isAssignableFrom(type)){
            return resultSet.getBlob(index).getBinaryStream();
        }else if(Reader.class.isAssignableFrom(type)) {
            return resultSet.getClob(index).getCharacterStream();
        }
        throw new QueryException("unsupported sql type :"+type + " of "+columnMeta);
    }

    public static Object get(ResultSet resultSet, int index, Class type, String columnMeta) throws SQLException {
        if(type == String.class){
            return resultSet.getString(index);
        }else if(type == Boolean.class || type == boolean.class){
            return resultSet.getBoolean(index);
        }else if(type == Byte.class || type == byte.class){
            return resultSet.getByte(index);
        }else if(type == Short.class || type == short.class){
            return resultSet.getShort(index);
        }else if(type == Integer.class || type == int.class){
            return resultSet.getInt(index);
        }else if(type == Long.class || type == long.class){
            return resultSet.getLong(index);
        }else if(type == Float.class || type == float.class){
            return resultSet.getFloat(index);
        }else if(type == Double.class || type == double.class){
            return resultSet.getDouble(index);
        }else if(type == BigDecimal.class){
            return resultSet.getBigDecimal(index);
        }else if(type == byte[].class){
            return resultSet.getBytes(index);
        }else if(type == java.util.Date.class){ //这里需要转化一下
            long time = resultSet.getDate(index).getTime();
            return new java.util.Date(time);
        }else if(type == java.sql.Date.class){
            return resultSet.getDate(index);
        }else if(type == java.sql.Time.class){
            return resultSet.getTime(index);
        }else if(type == java.sql.Timestamp.class){
            return resultSet.getTimestamp(index);
        }else if(type==Blob.class){
            return resultSet.getBlob(index);
        }else if(type == Clob.class){
            return resultSet.getClob(index);
        }else if(InputStream.class.isAssignableFrom(type)){
            return resultSet.getBlob(index).getBinaryStream();
        }else if(Reader.class.isAssignableFrom(type)) {
            return resultSet.getClob(index).getCharacterStream();
        }
        throw new QueryException("unsupported sql type :"+type + " of "+columnMeta);
    }
}
