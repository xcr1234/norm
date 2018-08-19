package org.norm.core.generator;

import org.norm.core.meta.ColumnMeta;
import org.norm.core.meta.Meta;
import org.norm.core.parameter.ColumnParameter;
import org.norm.core.parameter.Parameter;
import org.norm.core.query.UpdateQuery;
import org.norm.util.sql.AbstractSQL;

import java.util.ArrayList;
import java.util.List;

public class UpdateGenerator extends AbstractSQL<UpdateGenerator>{

    private List<Parameter> parameters = new ArrayList<Parameter>();


    public UpdateGenerator addParam(ColumnMeta columnMeta, Object object){
        SET(columnMeta.getColumnName() + "=?");
        parameters.add(new ColumnParameter(columnMeta,object));
        return this;
    }

    @Override
    public UpdateGenerator getSelf() {
        return this;
    }


    public UpdateQuery build(){
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setSql(toString());
        updateQuery.setParameters(parameters);
        return updateQuery;
    }
}
