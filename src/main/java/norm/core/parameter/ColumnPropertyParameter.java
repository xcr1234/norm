package norm.core.parameter;

import norm.core.meta.ColumnMeta;
import norm.util.ErrorContext;
import norm.util.JdbcUtils;

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
        if(columnMeta.getTypeConverter() != null){
            columnMeta.getTypeConverter().setParameter(ps,index,value);
        }else{
            JdbcUtils.setParameter(ps,index,value,columnMeta.getConfiguration().getJdbcNullType(),columnMeta.getJdbcType());
        }
    }

    @Override
    public Object getValue() {
        return columnMeta.get(object);
    }

    @Override
    public String getName() {
        return columnMeta.getName();
    }
}
