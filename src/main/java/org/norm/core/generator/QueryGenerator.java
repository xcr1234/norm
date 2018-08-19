package org.norm.core.generator;

import org.norm.core.handler.ResultSetHandler;
import org.norm.core.query.SelectQuery;
import org.norm.core.query.UpdateQuery;

public interface QueryGenerator {
    SelectQuery<?> select(String id, Object object);
    UpdateQuery update(String id, Object object);
    ResultSetHandler<?> getResultSetHandler();
}
