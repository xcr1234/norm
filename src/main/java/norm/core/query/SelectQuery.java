package norm.core.query;

import norm.core.handler.ResultSetHandler;
import norm.core.parameter.Parameter;

public class SelectQuery<T> implements Query{
    private String sql;
    private Iterable<Parameter> parameters;
    private ResultSetHandler<T> resultSetHandler;
    @Override
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
    @Override
    public Iterable<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Iterable<Parameter> parameters) {
        this.parameters = parameters;
    }

    public ResultSetHandler<T> getResultSetHandler() {
        return resultSetHandler;
    }

    public void setResultSetHandler(ResultSetHandler<T> resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
    }
}
