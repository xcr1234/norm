package norm.core.query;

import norm.core.meta.ColumnMeta;
import norm.core.parameter.Parameter;

public class UpdateQuery implements Query{
    private String sql;
    private Iterable<Parameter> parameters;
    private ReturnGenerateId returnGenerateId;


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

    public ReturnGenerateId getReturnGenerateId() {
        return returnGenerateId;
    }

    public void setReturnGenerateId(ReturnGenerateId returnGenerateId) {
        this.returnGenerateId = returnGenerateId;
    }
}
