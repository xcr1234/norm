package org.norm.core.parameter;

import org.norm.util.ErrorContext;
import org.norm.util.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ValueParameter implements Parameter{
    private String property;
    private Object value;

    public ValueParameter(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException {
        ErrorContext.instance().addParam(value);
        JdbcUtils.setParameter(ps,index,value, Types.NULL,null);
    }

    @Override
    public String getErrInfo() {
        return property;
    }
}
