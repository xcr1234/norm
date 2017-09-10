package impl;

import norm.generator.Converter;

public class DefaultConverter implements Converter {
    @Override
    public Class getJavaType(int sqlType, String sqlTypeName) {
        return null;
    }

    @Override
    public String getJavaName(String columnName) {
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
                sb.append(c);
                flag = false;
            }
        }
        return sb.toString();
    }
}
