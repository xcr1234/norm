package norm.core.interceptor;

import norm.JdbcTemplate;
import norm.core.handler.MapResultSetHandler;
import norm.core.handler.ResultSetHandler;
import norm.core.handler.SingleValueResultSetHandler;
import norm.core.meta.ColumnMeta;
import norm.core.parameter.Parameter;
import norm.core.parameter.ValueParameter;
import norm.core.query.SelectQuery;
import norm.core.query.UpdateQuery;
import norm.util.AssertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {

    private final CrudProxy crudProxy;

    public JdbcTemplateImpl(CrudProxy crudProxy) {
        this.crudProxy = crudProxy;
    }


    @Override
    public String selectFields() {
        StringBuilder sb = new StringBuilder();
        sb.append(' ');
        boolean flag = false;
        for (ColumnMeta columnMeta : crudProxy.getGenerator().getMeta().getColumnMetas().values()) {
            if (flag) {
                sb.append(',');
            }
            if (columnMeta.select()) {
                sb.append(columnMeta.getColumnName());
            }
            flag = true;
        }
        sb.append(' ');
        return sb.toString();
    }

    @Override
    public String table() {
        return crudProxy.getGenerator().getMeta().getTableName();
    }

    @Override
    public String selectSql() {
        return " select" + selectFields() +"from " + table() + " ";
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getType() {
        return (T) crudProxy.getGenerator().getBeanClass();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultSetHandler<T> getResultSetHandler() {
        return (ResultSetHandler<T>) crudProxy.getGenerator().getResultSetHandler();
    }

    protected List<Parameter> parameters(Object... args) {
        List<Parameter> list = new ArrayList<Parameter>();
        for (int i = 0; i < args.length; i++) {
            list.add(new ValueParameter("#p" + i, args[i], crudProxy.getNorm().getJdbcNullType()));
        }
        return list;
    }

    @Override
    public int update(String sql, Object... args) {
        AssertUtils.notNull(sql, "sql");
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setSql(sql);
        updateQuery.setParameters(parameters(args));
        return crudProxy.executeUpdate(updateQuery);
    }

    @Override
    public <R> R queryOne(String sql, ResultSetHandler<R> handler, Object... args) {
        AssertUtils.notNull(sql, "sql");
        AssertUtils.notNull(handler, "handler");
        SelectQuery<R> query = new SelectQuery<R>();
        query.setSql(sql);
        query.setResultSetHandler(handler);
        query.setParameters(parameters(args));
        return crudProxy.selectOne(query);
    }

    @Override
    public <R> R queryOne(String sql, Class<R> type, Object... args) {
        return queryOne(sql, new SingleValueResultSetHandler<R>(type), args);
    }

    @Override
    public T queryOne(String sql, Object... args) {
        return queryOne(sql, getResultSetHandler(), args);
    }

    @Override
    public <R> List<R> queryList(String sql, ResultSetHandler<R> handler, Object... args) {
        AssertUtils.notNull(sql, "sql");
        AssertUtils.notNull(handler, "handler");
        SelectQuery<R> query = new SelectQuery<R>();
        query.setSql(sql);
        query.setResultSetHandler(handler);
        query.setParameters(parameters(args));
        return crudProxy.selectList(query);
    }

    @Override
    public <R> List<R> queryList(String sql, Class<R> type, Object... args) {
        return queryList(sql, new SingleValueResultSetHandler<R>(type), args);
    }

    @Override
    public List<T> queryList(String sql, Object... args) {
        return queryList(sql, getResultSetHandler(), args);
    }

    @Override
    public List<Map<String, Object>> queryMap(String sql, Object... args) {
        return queryList(sql, new MapResultSetHandler(), args);
    }
}
