package norm.convert;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库中是java.uti.Date，实体类中是long的转换器，init是日期格
 */
public class Date2LongConverter implements TypeConverter <java.util.Date,Long>{
    @Override
    public Long getObject(ResultSet resultSet, String columnName) throws SQLException {
        Date date = resultSet.getDate(columnName);
        return date == null ? null : date.getTime();
    }


    @Override
    public java.util.Date setParameter(Long value) {
        return new java.sql.Date(value);
    }

    @Override
    public void init(String arg) {

    }
}
