package org.norm.core.parameter;

import org.norm.Norm;
import org.norm.core.meta.ColumnMeta;
import org.norm.util.ErrorContext;
import org.norm.util.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ColumnPropertyParameter implements Parameter {


    private ColumnMeta columnMeta;
    private Object object;

    public ColumnPropertyParameter(ColumnMeta columnMeta, Object object) {
        this.columnMeta = columnMeta;
        this.object = object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException {
        Object value = columnMeta.get(object);
        ErrorContext.instance().addParam(value);
        if(columnMeta.getTypeConverter() != null){
            columnMeta.getTypeConverter().setParameter(ps,index,value);
        }else{
            JdbcUtils.setParameter(ps,index,value,columnMeta.getConfiguration().getJdbcNullType(),columnMeta.getJdbcType());
        }
    }

    @Override
    public String getErrInfo() {
        return columnMeta.getName();
    }
}
