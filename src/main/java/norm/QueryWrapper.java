package norm;

import norm.anno.Query;
import norm.util.AssertUtils;
import norm.util.sql.SQL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Object> parameters;
    private boolean null2IsNull;
    private List<Integer> operations;
    private List<Object> operationParameters;
    private Object entity;

    private static final int OP_WHERE = 1;
    private static final int OP_ORDER_BY = 2;
    private static final int OP_AND = 3;
    private static final int OP_OR = 4;

    public QueryWrapper() {
        this.operations = new ArrayList<Integer>();
        this.operationParameters = new ArrayList<Object>();
        this.parameters = new ArrayList<Object>();
        this.null2IsNull = true;
    }

    public QueryWrapper(QueryWrapper queryWrapper) {
        this.operations = new ArrayList<Integer>(queryWrapper.operations);
        this.operationParameters = new ArrayList<Object>(queryWrapper.operationParameters);
        this.parameters = new ArrayList<Object>(queryWrapper.parameters);
        this.entity = queryWrapper.entity;
        this.null2IsNull = queryWrapper.null2IsNull;
    }

    public Object getEntity() {
        return entity;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public boolean isNull2IsNull() {
        return null2IsNull;
    }

    public void setNull2IsNull(boolean null2IsNull) {
        this.null2IsNull = null2IsNull;
    }

    public QueryWrapper where(String condition){
        operations.add(OP_WHERE);
        operationParameters.add(condition);
        return this;
    }

    public QueryWrapper addEntity(Object object){
        this.entity = object;
        return this;
    }

    public QueryWrapper allEq(Map<String, Object> params) {
        return allEq(params, null2IsNull);
    }

    public QueryWrapper allEq(Map<String, Object> params, boolean null2IsNull) {
        AssertUtils.notNull(params, "params");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                if (null2IsNull) {
                    where(key + " is null");
                }
            } else {
                where(key + " = ?");
                parameters.add(value);
            }
        }
        return this;
    }

    public QueryWrapper eq(String key, Object value) {
        return eq(key, value, null2IsNull);
    }

    public QueryWrapper eq(String key, Object value, boolean null2IsNull) {
        AssertUtils.notNull(key, "the key");
        if (value == null) {
            if (null2IsNull) {
                where(key + " is null");
            }
        } else {
            where(key + " = ?");
            parameters.add(value);
        }
        return this;
    }

    public QueryWrapper condition(String key, String condition,Object value) {
        AssertUtils.notNull(key, "the key");
        where(key + " " + condition + " ?");
        parameters.add(value);
        return this;
    }

    public QueryWrapper between(String key, Object value1, Object value2) {
        AssertUtils.notNull(key, "the key");
        where("between ? and ?");
        parameters.add(value1);
        parameters.add(value2);
        return this;
    }

    public QueryWrapper notBetween(String key, Object value1, Object value2) {
        AssertUtils.notNull(key, "the key");
        where("not between ? and ?");
        parameters.add(value1);
        parameters.add(value2);
        return this;
    }

    public QueryWrapper like(String key, String value) {
        return like(key, value, true, true);
    }

    public QueryWrapper like(String key, String value, boolean left, boolean right) {
        AssertUtils.notNull(key, "the key");
        StringBuilder sb = new StringBuilder();
        if (left) {
            sb.append('%');
        }
        sb.append(value);
        if (right) {
            sb.append("%");
        }
        where(key + " like ?");
        parameters.add(sb.toString());
        return this;
    }

    public QueryWrapper isNull(String key) {
        AssertUtils.notNull(key, "the key");
        where(key + " is null");
        return this;
    }

    public QueryWrapper isNotNull(String key) {
        AssertUtils.notNull(key, "the key");
        where(key + " is not null");
        return this;
    }

    public QueryWrapper in(String key, Object... values) {
        AssertUtils.notNull(key, "the key");
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append(" in (");
        boolean f = false;
        for (Object value : values) {
            if (f) {
                sb.append(',');
            }
            sb.append('?');
            parameters.add(value);
            f = true;
        }
        sb.append(")");
        where(sb.toString());
        return this;
    }

    public QueryWrapper notIn(String key, Object... values) {
        AssertUtils.notNull(key, "the key");
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append(" not in (");
        boolean f = false;
        for (Object value : values) {
            if (f) {
                sb.append(',');
            }
            sb.append('?');
            parameters.add(value);
            f = true;
        }
        sb.append(")");
        where(sb.toString());
        return this;
    }

    public QueryWrapper orderBy(String... columns) {
        operations.add(OP_ORDER_BY);
        operationParameters.add(columns);
        return this;
    }

    public QueryWrapper and() {
        operations.add(OP_AND);
        operationParameters.add(null);
        return this;
    }

    public QueryWrapper or() {
        operations.add(OP_OR);
        operationParameters.add(null);
        return this;
    }

    public void eval(SQL sql) {
        for (int i = 0; i < operations.size(); i++) {
            int op = operations.get(i);
            Object para = operationParameters.get(i);
            switch (op) {
                case OP_WHERE:
                    sql.WHERE((String) para);
                    break;
                case OP_ORDER_BY:
                    sql.ORDER_BY((String[]) para);
                    break;
                case OP_AND:
                    sql.AND();
                    break;
                case OP_OR:
                    sql.OR();
                    break;
                default:
                    customEval(sql, op, para);
            }
        }
    }

    protected void customEval(SQL sql, int op, Object para) {

    }
}

