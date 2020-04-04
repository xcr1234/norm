package norm.core.query;

import norm.core.parameter.Parameter;

public interface Query {

    String getSql();

    Iterable<Parameter> getParameters();
}
