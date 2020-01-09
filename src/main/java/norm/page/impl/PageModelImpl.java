package norm.page.impl;

import norm.core.parameter.Parameter;
import norm.core.parameter.ValueParameter;
import norm.page.PageModel;
import norm.util.AssertUtils;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PageModelImpl implements PageModel {

    private final String sql;
    private final ValueParameter firstParameter;
    private final ValueParameter secondParameter;
    private int index = 0;

    public PageModelImpl(String sql, Integer first) {
        this(sql, first, null);
    }

    public PageModelImpl(String sql, Integer first, Integer second) {
        AssertUtils.notNull(sql,"the sql");
        AssertUtils.notNull(first,"the first page parameter");
        this.sql = sql;
        this.firstParameter = wrapIntValue(first);
        this.secondParameter = wrapIntValue(second);
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public ValueParameter getFirstParameter() {
        return firstParameter;
    }

    @Override
    public ValueParameter getSecondParameter() {
        return secondParameter;
    }

    private ValueParameter wrapIntValue(Integer value) {
        if (value == null) {
            return null;
        }
        return new ValueParameter("pageParameter#" + index++, value, Types.INTEGER);
    }

}
