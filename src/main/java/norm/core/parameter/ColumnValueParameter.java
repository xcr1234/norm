package norm.core.parameter;

import norm.core.meta.ColumnMeta;
import norm.util.ErrorContext;
import norm.util.JdbcUtils;

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
        if(columnMeta.getTypeConverter() != null){
            columnMeta.getTypeConverter().setParameter(ps,index,value);
        }else{
            JdbcUtils.setParameter(ps,index,value,columnMeta.getConfiguration().getJdbcNullType(),columnMeta.getJdbcType());
        }
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getName() {
        return columnMeta.getName();
    }
}
