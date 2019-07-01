package norm.core.query;

import norm.util.sql.AbstractSQL;

public abstract class DynamicSQL extends AbstractSQL<DynamicSQL> {

    @Override
    public DynamicSQL getSelf() {
        return this;
    }



}
