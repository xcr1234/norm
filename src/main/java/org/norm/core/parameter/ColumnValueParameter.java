package org.norm.core.parameter;

import org.norm.core.meta.ColumnMeta;
import org.norm.util.ErrorContext;
import org.norm.util.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ColumnValueParameter implements Parameter{
    private ColumnMeta columnMeta;
    private Object value;

    public ColumnValueParameter(ColumnMeta columnMeta, Object value) {
        this.columnMeta = columnMeta;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException {
        ErrorContext.instance().addParam(value);
        if(columnMeta.getTypeConverter() != null){
            columnMeta.getTypeConverter().setParameter(ps,index,value);
        }else{
            JdbcUtils.setParameter(ps,index,value,columnMeta.getConfiguration().getJdbcNullType(),columnMeta.getJdbcType());
        }
    }

    @Override
    public String getName() {
        return columnMeta.getName();
    }
}
