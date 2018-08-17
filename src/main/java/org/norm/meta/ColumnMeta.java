package org.norm.meta;

import org.norm.TypeConverter;

import java.lang.annotation.Annotation;

public interface ColumnMeta<T> {

    Class<?> getDeclaringType();

    <E extends Annotation> E getAnnotation(Class<E> type);

    String getColumnName();

    boolean select();

    boolean insert();

    boolean update();

    Object getValue(T obj);

    void setValue(T obj,Object value);

    TypeConverter<?> getTypeConverter();

}
