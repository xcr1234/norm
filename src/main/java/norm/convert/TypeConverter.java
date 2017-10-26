package norm.convert;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverter<Database,Entity> {
    Entity getObject(ResultSet resultSet, String columnName) throws SQLException;

    Database setParameter(Entity value);

    void init(String arg);
}
