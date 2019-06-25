package org.norm.core.generator;

import org.norm.QueryWrapper;
import org.norm.core.handler.ResultSetHandler;
import org.norm.core.query.SelectQuery;
import org.norm.core.query.UpdateQuery;
import org.norm.util.sql.SQL;

public interface QueryGenerator {
    SelectQuery<?> select(String id, Object object);
    UpdateQuery update(String id, Object object);
    ResultSetHandler<?> getResultSetHandler();
    SelectQuery<?> findAllQuery(QueryWrapper queryWrapper);
}
