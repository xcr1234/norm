package norm.convert;

import norm.util.AssertUtils;
import norm.util.JdbcUtils;
import norm.util.ReflectUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 默认的枚举类型处理器
 */
public class DefaultEnumConverter implements EnumConverter {
    @SuppressWarnings("unchecked")
    @Override
    public Enum getObject(ResultSet resultSet, String column, Class<Enum> type) throws SQLException {
        if (IEnum.class.isAssignableFrom(type)) {
            Method method = ReflectUtils.getMethodOrNull(type, "getValue");
            AssertUtils.notNull(method, "getValue() of class " + type.getName());
            Object value = JdbcUtils.getObject(resultSet, column, method.getReturnType());
            if (value == null) {
                return null;
            }
            Object array = type.getEnumConstants();
            for (int i = 0; i < Array.getLength(array); i++) {
                IEnum iEnum = (IEnum) Array.get(array, i);
                if (value.equals(iEnum.getValue())) {
                    return (Enum) iEnum;
                }
            }
            return null;
        } else {
            String result = resultSet.getString(column);
            if (result == null) {
                return null;
            }
            return Enum.valueOf(type, result);
        }
    }

    @Override
    public void setParameter(PreparedStatement ps, int index, Enum value, int nullType, Integer jdbcType) throws SQLException {
        if (value instanceof IEnum) {
            JdbcUtils.setParameter(ps, index, ((IEnum) value).getValue(), nullType, jdbcType);
        } else {
            JdbcUtils.setParameter(ps, index, value.name(), nullType, jdbcType);
        }
    }

    @Override
    public void init(String arg) {

    }
}
