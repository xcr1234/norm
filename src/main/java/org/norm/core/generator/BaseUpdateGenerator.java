package org.norm.core.generator;

import org.norm.core.QueryGenerator;
import org.norm.core.ResultSetHandler;
import org.norm.core.SelectQuery;

public abstract class BaseUpdateGenerator implements QueryGenerator{
    @Override
    public SelectQuery<?> select(String id, Object object) {
        throw new UnsupportedOperationException("Generator " + getClass().getName() + " can only generate update query methods");
    }

    @Override
    public ResultSetHandler<?> getResultSetHandler() {
        throw new UnsupportedOperationException("Generator " + getClass().getName() + " can only generate update query methods");
    }
}
