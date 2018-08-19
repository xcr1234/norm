package org.norm.core.parameter;

import org.norm.core.Parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ValueParameter implements Parameter{
    private String property;
    private Object value;

    public ValueParameter(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException {
        ps.setObject(index,value);
    }

    @Override
    public String getErrInfo() {
        return property;
    }
}
