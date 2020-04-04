package norm.convert;

import norm.TypeConverter;

import java.sql.*;

/**
 * 数据库是Date,实体是Long的转换器
 */
public class Date2LongConverter implements TypeConverter<Long> {
    @Override
    public Long getObject(ResultSet resultSet, String column,Class<Long> requiredType) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(column);
        if(timestamp == null){
            return null;
        }
        return timestamp.getTime();
    }

    @Override
    public void setParameter(PreparedStatement ps, int index, Long value) throws SQLException {
        if(value == null){
            ps.setNull(index, Types.TIMESTAMP);
        }else{
            ps.setTimestamp(index,new Timestamp(value));
        }
    }

    @Override
    public void init(String arg) {

    }
}
