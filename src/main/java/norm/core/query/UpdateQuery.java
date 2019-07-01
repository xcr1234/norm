package norm.core.query;

import norm.core.parameter.Parameter;

public class UpdateQuery{
    private String sql;
    private Iterable<Parameter> parameters;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Iterable<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Iterable<Parameter> parameters) {
        this.parameters = parameters;
    }
}
