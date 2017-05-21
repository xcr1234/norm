package norm.result;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 在这个接口中可以取出查询的结果，并自动对结果的类型进行转换。
 */
public interface QueryResultItem{

    Object getObject(String column);

    Object getObject(int index);

    Object getOgnl(String ognl);

    String getString(int index);

    Byte getByte(int index);

    Character getChar(int index);

    Short getShort(int index);

    BigDecimal getBigDecimal(int index);

    BigInteger getBigInteger(int index);

    Float getFloat(int index);

    Double getDouble(int index);

    Date getDate(int index);

    java.sql.Date getSQLDate(int index);

    Timestamp getTimestamp(int index);

    Long getLong(int index);

    Integer getInt(int index);

    byte[] getBytes(int index);

    Boolean getBoolean(int index);

    String getString(String column);

    Byte getByte(String column);

    Character getChar(String column);

    Short getShort(String column);

    BigDecimal getBigDecimal(String column);

    BigInteger getBigInteger(String column);

    Float getFloat(String column);

    Double getDouble(String column);

    Date getDate(String column);

    java.sql.Date getSQLDate(String column);

    Timestamp getTimestamp(String column);

    Long getLong(String column);

    Integer getInt(String column);

    byte[] getBytes(String column);

    Boolean getBoolean(String column);


}
