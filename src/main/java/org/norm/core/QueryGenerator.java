package org.norm.core;

public interface QueryGenerator {
    SelectQuery<?> select(String id,Object object);
    UpdateQuery update(String id,Object object);
    ResultSetHandler<?> getResultSetHandler();
}
