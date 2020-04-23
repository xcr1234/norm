package norm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverter<T> {

    T getObject(ResultSet resultSet,String column,Class<T> requiredType) throws SQLException;

    void setParameter(PreparedStatement ps,int index,T value,int nullType,Integer jdbcType) throws SQLException;

    void init(String arg);
}
