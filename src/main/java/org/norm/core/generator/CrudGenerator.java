package org.norm.core.generator;

import org.norm.Configuration;
import org.norm.core.handler.ResultSetHandler;
import org.norm.core.meta.ColumnMeta;
import org.norm.core.meta.Meta;
import org.norm.core.query.SelectQuery;
import org.norm.core.query.UpdateQuery;
import org.norm.util.ErrorContext;

public class CrudGenerator implements QueryGenerator{

    private Class<?> beanClass;
    private Meta meta;

    public CrudGenerator(Configuration configuration,Class<?> beanClass) {
        ErrorContext.instance().setConfiguration(configuration);
        this.beanClass = beanClass;
        this.meta = Meta.parse(beanClass,configuration);
    }

    @Override
    public SelectQuery<?> select(String id, Object object) {
        return null;
    }

    @Override
    public UpdateQuery update(String id, Object object) {
        if(GeneratorIds.UPDATE.equals(id)){
            return updateGenerator(object);
        }
        return null;
    }

    protected UpdateQuery updateGenerator(final Object object){
        return new UpdateGenerator(){{
            UPDATE(meta.getTableName());
            for(ColumnMeta columnMeta : meta.getColumnMetas().values()){
                if(columnMeta.update()){
                    addParam(columnMeta,object);
                }
            }
        }}.build();
    }

    @Override
    public ResultSetHandler<?> getResultSetHandler() {
        return null;
    }
}
