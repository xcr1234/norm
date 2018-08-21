package org.norm.core.generator;

import org.norm.Norm;

public class DefaultGeneratorFactory implements QueryGeneratorFactory {

    private Norm norm;

    public DefaultGeneratorFactory(Norm norm) {
        this.norm = norm;
    }

    @Override
    public QueryGenerator getGenerator(Class<?> type) {
        return new CrudGenerator(norm,type);
    }
}
