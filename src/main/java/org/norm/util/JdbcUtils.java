package org.norm.util;


import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;

public final class JdbcUtils {

    public static void setParameter(PreparedStatement ps,int index,Object value,int nullType,Integer type) throws SQLException{
        if(value == null){
            ps.setNull(index,nullType);
        }else if(type != null){
            ps.setObject(index,value,type);
        }else{
            setParameter(ps,index,value);
        }
    }

    public static void setParameter(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value instanceof String) {
            ps.setString(index, (String) value);
        } else if (value instanceof Boolean) {
            ps.setBoolean(index, (Boolean) value);
        } else if (value instanceof Byte) {
            ps.setByte(index, (Byte) value);
        } else if (value instanceof Short) {
            ps.setShort(index, (Short) value);
        } else if (value instanceof Integer) {
            ps.setInt(index, (Integer) value);
        } else if (value instanceof Long) {
            ps.setLong(index, (Long) value);
        } else if (value instanceof Float) {
            ps.setFloat(index, (Float) value);
        } else if (value instanceof Double) {
            ps.setDouble(index, (Double) value);
        } else if (value instanceof BigDecimal) {
            ps.setBigDecimal(index, (BigDecimal) value);
        } else if (value instanceof byte[]) {
            ps.setBytes(index, (byte[]) value);
        } else if (value instanceof Timestamp) {
            ps.setTimestamp(index, (Timestamp) value);
        } else if (value instanceof java.sql.Date) {
            ps.setDate(index, (java.sql.Date) value);
        } else if (value instanceof Time) {
            ps.setTime(index, (Time) value);
        } else if (value instanceof java.util.Date) {
            Timestamp timestamp = new Timestamp(((java.util.Date) value).getTime());
            ps.setTimestamp(index, timestamp);
        } else if (value instanceof Blob) {
            ps.setBlob(index, (Blob) value);
        } else if (value instanceof InputStream) {
            ps.setBlob(index, (InputStream) value);
        } else if (value instanceof Clob) {
            ps.setClob(index, (Clob) value);
        } else if (value instanceof Reader) {
            ps.setClob(index, (Reader) value);
        } else {
            ps.setObject(index, value);
        }
    }



    public static Object getObject(ResultSet resultSet, String index, Class<?> type)throws SQLException{
        if(type == String.class){
            return resultSet.getString(index);
        }else if(type == Boolean.class || type == boolean.class){
            boolean b = resultSet.getBoolean(index);
            if(resultSet.wasNull()){
                return null;
            }
            return b;
        }else if(type == Byte.class || type == byte.class){
            byte b = resultSet.getByte(index);
            if(resultSet.wasNull()){
                return null;
            }
            return b;
        }else if(type == Short.class || type == short.class){
            short s = resultSet.getShort(index);
            if(resultSet.wasNull()){
                return null;
            }
            return s;
        }else if(type == Integer.class || type == int.class){
            int i = resultSet.getInt(index);
            if(resultSet.wasNull()){
                return null;
            }
            return i;
        }else if(type == Long.class || type == long.class){
            long l = resultSet.getLong(index);
            if(resultSet.wasNull()){
                return null;
            }
            return l;
        }else if(type == Float.class || type == float.class){
            float f = resultSet.getFloat(index);
            if(resultSet.wasNull()){
                return null;
            }
            return f;
        }else if(type == Double.class || type == double.class){
            double d =  resultSet.getDouble(index);
            if(resultSet.wasNull()){
                return null;
            }
            return d;
        }else if(type == BigDecimal.class){
            return resultSet.getBigDecimal(index);
        }else if(type == byte[].class){
            return resultSet.getBytes(index);
        }else if(type == java.util.Date.class){
            Timestamp timestamp = resultSet.getTimestamp(index);
            return timestamp == null ? null : new java.util.Date(timestamp.getTime());
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
            Blob blob = resultSet.getBlob(index);
            if(blob == null){
                return null;
            }
            return resultSet.getBlob(index).getBinaryStream();
        }else if(Reader.class.isAssignableFrom(type)) {
            Clob clob = resultSet.getClob(index);
            if(clob == null){
                return null;
            }
            return clob.getCharacterStream();
        }
        throw new SQLException("can't get object:unsupported type :"+type);
    }

    public static Object getObject(ResultSet resultSet, int index, Class<?> type)throws SQLException{
        if(type == String.class){
            return resultSet.getString(index);
        }else if(type == Boolean.class || type == boolean.class){
            boolean b = resultSet.getBoolean(index);
            if(resultSet.wasNull()){
                return null;
            }
            return b;
        }else if(type == Byte.class || type == byte.class){
            byte b = resultSet.getByte(index);
            if(resultSet.wasNull()){
                return null;
            }
            return b;
        }else if(type == Short.class || type == short.class){
            short s = resultSet.getShort(index);
            if(resultSet.wasNull()){
                return null;
            }
            return s;
        }else if(type == Integer.class || type == int.class){
            int i = resultSet.getInt(index);
            if(resultSet.wasNull()){
                return null;
            }
            return i;
        }else if(type == Long.class || type == long.class){
            long l = resultSet.getLong(index);
            if(resultSet.wasNull()){
                return null;
            }
            return l;
        }else if(type == Float.class || type == float.class){
            float f = resultSet.getFloat(index);
            if(resultSet.wasNull()){
                return null;
            }
            return f;
        }else if(type == Double.class || type == double.class){
            double d =  resultSet.getDouble(index);
            if(resultSet.wasNull()){
                return null;
            }
            return d;
        }else if(type == BigDecimal.class){
            return resultSet.getBigDecimal(index);
        }else if(type == byte[].class){
            return resultSet.getBytes(index);
        }else if(type == java.util.Date.class){
            Timestamp timestamp = resultSet.getTimestamp(index);
            return timestamp == null ? null : new java.util.Date(timestamp.getTime());
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
            Blob blob = resultSet.getBlob(index);
            if(blob == null){
                return null;
            }
            return resultSet.getBlob(index).getBinaryStream();
        }else if(Reader.class.isAssignableFrom(type)) {
            Clob clob = resultSet.getClob(index);
            if(clob == null){
                return null;
            }
            return clob.getCharacterStream();
        }
        throw new SQLException("can't get object:unsupported type :"+type);
    }
}
