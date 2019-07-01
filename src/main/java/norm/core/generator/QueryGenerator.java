package norm.core.generator;

import norm.QueryWrapper;
import norm.core.handler.ResultSetHandler;
import norm.core.query.SelectQuery;
import norm.core.query.UpdateQuery;

public interface QueryGenerator {
    SelectQuery<?> select(String id, Object object);
    UpdateQuery update(String id, Object object);
    ResultSetHandler<?> getResultSetHandler();
    SelectQuery<?> findAllQuery(QueryWrapper queryWrapper);
}
