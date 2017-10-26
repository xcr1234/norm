package norm.convert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 数据库中是long，实体类中是java.uti.Date的转换器
 */
public class Long2DateConverter implements TypeConverter<Long,Date>{
    @Override
    public Date getObject(ResultSet resultSet, String columnName) throws SQLException {
        Long l = resultSet.getLong(columnName);
        if(l == null || l == 0){
            return null;
        }
        return new Date(l);
    }


    @Override
    public Long setParameter(Date value) {
        return value.getTime();
    }

    @Override
    public void init(String arg) {

    }
}
