package norm.convert;



import norm.QueryException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据库中是java.uti.Date，实体类中是字符串String的转换器，init是日期格式
 */
public class Date2StrConverter implements TypeConverter<Date,String>{


    private SimpleDateFormat simpleDateFormat;
    private String format = defaultDateFormat;

    private static String defaultDateFormat = "yyyyMMdd";

    public static String getDefaultDateFormat() {
        return defaultDateFormat;
    }

    public static void setDefaultDateFormat(String defaultDateFormat) {
        Date2StrConverter.defaultDateFormat = defaultDateFormat;
    }

    @Override
    public String getObject(ResultSet resultSet, String columnName) throws SQLException {
        java.sql.Date sqlDate = resultSet.getDate(columnName);
        if(sqlDate == null){
            return null;
        }
        Date date = new Date(sqlDate.getTime());
        synchronized (this){
            return this.simpleDateFormat.format(date);
        }
    }


    @Override
    public Date setParameter(String value) {
        if(value == null){
            return null;
        }
        synchronized (this){
            try {
                return this.simpleDateFormat.parse(value);
            } catch (ParseException e) {
                throw new QueryException("invalid date string: [" + value + "] with format : " + this.format);
            }
        }
    }

    @Override
    public void init(String arg) {
        if(arg.isEmpty()){
            this.simpleDateFormat = new SimpleDateFormat(defaultDateFormat);
        }else{
            this.simpleDateFormat = new SimpleDateFormat(arg);
            this.format = arg;
        }
    }
}
