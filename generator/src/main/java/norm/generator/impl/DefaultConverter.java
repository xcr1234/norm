package norm.generator.impl;

import norm.generator.Column;
import norm.generator.Converter;
import norm.generator.Entity;
import org.apache.commons.lang.StringUtils;

import static java.sql.Types.*;

public class DefaultConverter implements Converter {

    @Override
    public Class getJavaType(int sqlType, String sqlTypeName, Column column) {
        switch (sqlType){
            case BIT : case TINYINT : case SMALLINT:
            case INTEGER:case NUMERIC:case BIGINT:case FLOAT:
            case REAL: case DOUBLE:  case DECIMAL:
                return getNumberType(sqlType,sqlTypeName,column);
            case CHAR: case VARCHAR: case LONGVARCHAR: case NCHAR: case NVARCHAR: case LONGNVARCHAR:return String.class;
            case DATE:return java.util.Date.class;
            case TIME:return java.sql.Time.class;
            case TIMESTAMP:return java.sql.Timestamp.class;
            case BLOB:return java.sql.Blob.class;
            case CLOB:return java.sql.Clob.class;
            case BOOLEAN:return Boolean.class;
            case ARRAY:return java.sql.Array.class;

        }
        throw new UnsupportedOperationException("unsupported sql type:" + sqlTypeName);
    }

    protected Class getNumberType(int sqlType, String sqlTypeName, Column column){
        switch (sqlType){
            case BIT : return Byte.class;
            case TINYINT : case SMALLINT: return Short.class;
            case INTEGER:case NUMERIC:return Integer.class;
            case BIGINT:return Long.class;
            case FLOAT:return Float.class;
            case REAL: case DOUBLE:  case DECIMAL:
                return Double.class;
        }
        throw new UnsupportedOperationException("unsupported sql type:" + sqlTypeName);
    }

    @Override
    public String getJavaName(String columnName,Column column,Entity entity) {
        StringBuilder sb = new StringBuilder(columnName.length() + 6);
        boolean flag = false;
        for(int i=0;i<columnName.length();i++){

            char c = columnName.charAt(i);
            if(c == '_'){
                flag = true;
            }else if(flag || i == 0){
                sb.append(Character.toUpperCase(c));
                flag = false;
            }else{
                sb.append(Character.toLowerCase(c));
                flag = false;
            }
        }
        if(column != null){
            return StringUtils.uncapitalize(sb.toString());
        }
        return sb.toString();
    }
}
