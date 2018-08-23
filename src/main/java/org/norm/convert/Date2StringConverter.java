package org.norm.convert;

import org.norm.TypeConverter;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据库是Date,实体是String的转换器
 */
public class Date2StringConverter implements TypeConverter<String> {

    private String format;

    private static String defaultFormat = "yyyyMMdd HH:mm:ss";

    public static String getDefaultFormat() {
        return defaultFormat;
    }

    public static void setDefaultFormat(String defaultFormat) {
        Date2StringConverter.defaultFormat = defaultFormat;
    }

    @Override
    public String getObject(ResultSet resultSet, String column) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(column);
        if(timestamp == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(timestamp.getTime()));
    }

    @Override
    public void setParameter(PreparedStatement ps, int index, String value) throws SQLException {
        if(value == null){
            ps.setNull(index, Types.TIMESTAMP);
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date date = null;
            try {
                date = simpleDateFormat.parse(value);
            } catch (ParseException e) {
                throw new SQLException("cannot parse date " + value + " with format " + format ,e);
            }
            ps.setTimestamp(index,new Timestamp(date.getTime()));
        }
    }

    @Override
    public void init(String arg) {
        if(arg.isEmpty()){
            format = defaultFormat;
        }else{
            format = arg;
        }
    }
}
