package org.norm.result;

import org.norm.TypeConverter;

public class Parameter {

    private String property;

    private Integer jdbcType;

    private TypeConverter<?> converter;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(Integer jdbcType) {
        this.jdbcType = jdbcType;
    }

    public TypeConverter<?> getConverter() {
        return converter;
    }

    public void setConverter(TypeConverter<?> converter) {
        this.converter = converter;
    }
}
