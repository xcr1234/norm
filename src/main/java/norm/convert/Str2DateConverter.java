package norm.convert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 实体类是java.uti.Date，数据库中是字符串String的转换器，init是日期格式
 */
public class Str2DateConverter implements TypeConverter<String,Date> {


    private SimpleDateFormat simpleDateFormat;
    private String format = defaultDateFormat;

    private static String defaultDateFormat = "yyyyMMdd";

    public static String getDefaultDateFormat() {
        return defaultDateFormat;
    }

    public static void setDefaultDateFormat(String defaultDateFormat) {
        Str2DateConverter.defaultDateFormat = defaultDateFormat;
    }



    @Override
    public Date getObject(ResultSet resultSet, String columnName) throws SQLException {
        String str = resultSet.getString(columnName);
        if(str == null){
            return null;
        }
        synchronized (this){
            try {
                return this.simpleDateFormat.parse(str);
            } catch (ParseException e) {
                throw new SQLException("invalid date string: [" + str + "] with format : " + this.format);
            }
        }
    }


    @Override
    public String setParameter(Date date)  {
        synchronized (this){
            return this.simpleDateFormat.format(date);
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
