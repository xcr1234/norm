package org.norm.core.query;

import org.norm.util.sql.AbstractSQL;

public abstract class DynamicSQL extends AbstractSQL<DynamicSQL> {

    @Override
    public DynamicSQL getSelf() {
        return this;
    }



}
