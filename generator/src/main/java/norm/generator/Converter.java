package norm.generator;

public interface Converter {

    Class getJavaType(int sqlType,String sqlTypeName);

    String getJavaName(String columnName);



}
