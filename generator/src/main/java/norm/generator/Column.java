package norm.generator;

import java.sql.SQLException;

public interface Column {

    Class getJavaType() throws SQLException;

    /**
     * @see java.sql.Types
     * @return JDBC数据类型。
     */
    int getSQLType() throws SQLException;

    String getSQLTypeName() throws SQLException;

    String getJavaName();

    String getColumnName();

    String getGetterName();

    String getSetterName();


}
