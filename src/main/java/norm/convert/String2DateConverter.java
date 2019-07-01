package norm.convert;

import norm.TypeConverter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据库是String,实体是Date的转换器
 */
public class String2DateConverter implements TypeConverter<Date> {

    private String format;

    private static String defaultFormat = "yyyyMMdd HH:mm:ss";

    public static String getDefaultFormat() {
        return defaultFormat;
    }

    public static void setDefaultFormat(String defaultFormat) {
        String2DateConverter.defaultFormat = defaultFormat;
    }


    @Override
    public Date getObject(ResultSet resultSet, String column) throws SQLException {
        String value = resultSet.getString(column);
        if(value == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            throw new SQLException("cannot parse date " + value + " with format " + format ,e);
        }
    }

    @Override
    public void setParameter(PreparedStatement ps, int index, Date value) throws SQLException {
        if(value == null){
            ps.setString(index,null);
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            ps.setString(index,simpleDateFormat.format(value));
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
