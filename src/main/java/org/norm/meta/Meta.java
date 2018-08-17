package org.norm.meta;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface Meta {

    String getTableName();

    Class<?> getDeclaringType();

    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    ColumnMeta<?> getIdColumn();

    Collection<? extends ColumnMeta> getColumns();

    ColumnMeta<?> getColumn(String name);

    Object newInstance();
}
