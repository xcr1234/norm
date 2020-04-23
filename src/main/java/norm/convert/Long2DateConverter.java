package norm.convert;

import norm.TypeConverter;

import java.sql.*;
import java.util.Date;


/**
 * 数据库是Long,实体是Date的转换器
 */
public class Long2DateConverter implements TypeConverter<Date> {

    @Override
    public Date getObject(ResultSet resultSet, String column,Class<Date> requiredType) throws SQLException {
        long l = resultSet.getLong(column);
        if(resultSet.wasNull()){
            return null;
        }
        return new Date(l);
    }

    @Override
    public void setParameter(PreparedStatement ps, int index, Date value,int nullType,Integer jdbcType) throws SQLException {
        if(value == null){
            ps.setNull(index, Types.BIGINT);
        }else{
            ps.setLong(index,value.getTime());
        }
    }

    @Override
    public void init(String arg) {

    }
}
