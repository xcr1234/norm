package org.norm.result;

import org.norm.TypeConverter;

public class Result {

    private String property;

    private String column;

    private TypeConverter<?> converter;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }


    public TypeConverter<?> getConverter() {
        return converter;
    }

    public void setConverter(TypeConverter<?> converter) {
        this.converter = converter;
    }
}
