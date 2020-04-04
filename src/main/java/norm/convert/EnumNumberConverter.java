package norm.convert;

import norm.TypeConverter;
import norm.exception.ExecutorException;
import norm.util.ReflectUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 数据库是int,实体是Enum的转换器
 */
public class EnumNumberConverter implements TypeConverter<Enum> {


    @Override
    public Enum getObject(ResultSet resultSet, String column, Class<Enum> requiredType) throws SQLException {
        int value = resultSet.getInt(column);
        if (resultSet.wasNull()) {
            return null;
        }
        Object array = requiredType.getEnumConstants();
        return (Enum) Array.get(array, value);
    }

    @Override
    public void setParameter(PreparedStatement ps, int index, Enum value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value.ordinal());
        }
    }

    @Override
    public void init(String arg) {

    }
}
