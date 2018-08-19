package org.norm.core.parameter;

import org.norm.Configuration;
import org.norm.core.meta.ColumnMeta;
import org.norm.core.meta.Meta;
import org.norm.util.ErrorContext;
import org.norm.util.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ColumnParameter implements Parameter {

    private ColumnMeta columnMeta;
    private Object object;

    public ColumnParameter(ColumnMeta columnMeta, Object object) {
        this.columnMeta = columnMeta;
        this.object = object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException {
        Configuration configuration = ErrorContext.instance().getConfiguration();
        Object value = columnMeta.get(object);
        ErrorContext.instance().addParam(value);
        if(columnMeta.getTypeConverter() != null){
            columnMeta.getTypeConverter().setParameter(ps,index,value);
        }else{
            JdbcUtils.setParameter(ps,index,value,configuration.getJdbcNullType(),columnMeta.getJdbcType());
        }
    }

    @Override
    public String getErrInfo() {
        return columnMeta.getName();
    }
}
