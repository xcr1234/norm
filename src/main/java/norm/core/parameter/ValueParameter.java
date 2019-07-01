package norm.core.parameter;

import norm.util.ErrorContext;
import norm.util.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ValueParameter implements Parameter{
    private String property;
    private Object value;

    private int nullType;


    public ValueParameter(String property, Object value, int nullType) {
        this.property = property;
        this.value = value;
        this.nullType = nullType;
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException {
        ErrorContext.instance().addParam(value);
        JdbcUtils.setParameter(ps,index,value, nullType,null);
    }

    @Override
    public String getName() {
        return property;
    }
}
