package norm.core.generator;

import norm.Norm;
import norm.QueryWrapper;
import norm.anno.Id;
import norm.anno.OrderBy;
import norm.anno.OrderBys;
import norm.core.handler.CrudResultSetHandler;
import norm.core.handler.ResultSetHandler;
import norm.core.handler.SingleValueResultSetHandler;
import norm.core.meta.ColumnMeta;
import norm.core.meta.Meta;
import norm.core.parameter.ArrayParameters;
import norm.core.parameter.ColumnPropertyParameter;
import norm.core.parameter.ColumnValueParameter;
import norm.core.parameter.Parameter;
import norm.core.query.SelectQuery;
import norm.core.query.SelectQueryMiddle;
import norm.core.query.UpdateQuery;
import norm.exception.ExecutorException;
import norm.util.sql.SQL;

import java.util.*;

public class CrudGenerator implements QueryGenerator{

    protected Norm norm;
    protected Class<?> beanClass;
    protected Meta meta;
    private ResultSetHandler resultSetHandler;

    public CrudGenerator(Norm norm, Class<?> beanClass) {
        this.norm = norm;
        this.beanClass = beanClass;
        this.meta = Meta.parse(beanClass,norm.getConfiguration());
        this.resultSetHandler = generateResultSetHandler();
    }

    @Override
    public Meta getMeta() {
        return meta;
    }

    @Override
    public SelectQuery<?> select(String id, Object object) {
        if(GeneratorIds.EXISTS.equals(id)){
            return exists(object);
        }
        if(GeneratorIds.COUNT.equals(id)){
            return count(object);
        }
        if(GeneratorIds.FIND_ONE.equals(id)){
            return findOneGenerator(object);
        }
        if(GeneratorIds.FIND_ALL.equals(id)){
            if(object == null){
                return findAllGenerator();
            }else{
                return findAllFilterGenerator(object);
            }
        }
        return customSelect(id,object);
    }

    @Override
    public UpdateQuery update(String id, Object object) {
        if(GeneratorIds.SAVE.equals(id)){
            return saveGenerator(object);
        }
        if(GeneratorIds.DELETE.equals(id)){
            return deleteGenerator(object);
        }
        if(GeneratorIds.DELETE_BY_ID.equals(id)){
            return deleteByIdGenerator(object);
        }
        if(GeneratorIds.UPDATE.equals(id)){
            return updateGenerator(object);
        }
        return customUpdate(id,object);
    }

    protected UpdateQuery customUpdate(String id, Object object){
        throw new IllegalArgumentException("cannot generate update query with id :" + id);
    }

    protected SelectQuery<?> customSelect(String id, Object object){
        throw new IllegalArgumentException("cannot generate select query with id :" + id);
    }

    protected UpdateQuery updateGenerator(final Object object){
        final ColumnMeta idColumnMeta = meta.getIdColumn();
        Object idValue = idColumnMeta.get(object);
        if(idValue == null){
            throw new IllegalArgumentException("cannot update while id is null.");
        }
        final List<Parameter> parameters = new ArrayList<Parameter>();
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setSql(new SQL(){{
            boolean updateFlag = true;
            UPDATE(meta.getTableName());
            for(ColumnMeta columnMeta : meta.getColumnMetas().values()){
                if(columnMeta.update()){
                    if(norm.isUpdateNulls()){
                        SET(columnMeta.getColumnName() + " = ?");
                        parameters.add(new ColumnPropertyParameter(columnMeta,object));
                    }else{
                        Object value = columnMeta.get(object);
                        if(value != null){
                            updateFlag = false;
                            SET(columnMeta.getColumnName() + " = ?");
                            parameters.add(new ColumnValueParameter(columnMeta,value));
                        }
                    }
                }
            }
            if(!norm.isUpdateNulls() && updateFlag){
                throw new ExecutorException("no column will be updated,all values of column is null.");
            }
            WHERE(idColumnMeta.getColumnName() + "= ?");
        }}.toString());
        parameters.add(new ColumnValueParameter(idColumnMeta,idValue));
        updateQuery.setParameters(parameters);
        return updateQuery;
    }

    protected UpdateQuery saveGenerator(final Object object){
        final List<Parameter> parameters = new ArrayList<Parameter>();
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setSql(new SQL(){{
            INSERT_INTO(meta.getTableName());
            for(ColumnMeta columnMeta : meta.getColumnMetas().values()){
                if(columnMeta.insert()){
                    Id id = columnMeta.getAnnotation(Id.class);
                    if(id == null){
                        VALUES(columnMeta.getColumnName(),"?");
                        parameters.add(new ColumnPropertyParameter(columnMeta,object));
                    }else if(!id.value().isEmpty()){
                        VALUES(columnMeta.getColumnName(),id.value());
                    }else if(!id.identity()){
                        VALUES(columnMeta.getColumnName(),"?");
                        parameters.add(new ColumnPropertyParameter(columnMeta,object));
                    }
                }
            }
        }}.toString());
        updateQuery.setParameters(parameters);
        return updateQuery;
    }

    protected UpdateQuery deleteByIdGenerator(final Object object){
        if(object == null){
            throw new IllegalArgumentException("cannot delete while id is null.");
        }
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setSql(new SQL(){{
            DELETE_FROM(meta.getTableName());
            WHERE(meta.getIdColumn().getColumnName() + " = ?");
        }}.toString());
        updateQuery.setParameters(Collections.<Parameter>singletonList(new ColumnValueParameter(meta.getIdColumn(),object)));
        return updateQuery;
    }

    protected UpdateQuery deleteGenerator(final Object object){
        final List<Parameter> parameters = new ArrayList<Parameter>();
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setSql(new SQL(){{
            DELETE_FROM(meta.getTableName());
            for(ColumnMeta columnMeta : meta.getColumnMetas().values()){
                Object value = columnMeta.get(object);
                if(value != null){
                    WHERE(columnMeta.getColumnName() + " = ?");
                    parameters.add(new ColumnValueParameter(columnMeta,value));
                }
            }
        }}.toString());
        updateQuery.setParameters(parameters);
        return updateQuery;
    }



    protected SelectQuery<Integer> exists(final Object object){
        if(object == null){
            throw new IllegalArgumentException("cannot select exists while id is null!");
        }
        SelectQuery<Integer> selectQuery = new SelectQuery<Integer>();
        selectQuery.setSql(new SQL(){{
            SELECT("1");
            FROM(meta.getTableName());
            WHERE(meta.getIdColumn().getColumnName() + " = ?");
        }}.toString());
        selectQuery.setParameters(Collections.<Parameter>singletonList(new ColumnValueParameter(meta.getIdColumn(),object)));
        selectQuery.setResultSetHandler(new SingleValueResultSetHandler<Integer>(Integer.class));
        return selectQuery;
    }

    protected SelectQuery<Integer> count(final Object object){
        final List<Parameter> parameters = new ArrayList<Parameter>();
        SelectQuery<Integer> selectQuery = new SelectQuery<Integer>();
        selectQuery.setSql(new SQL(){{
            SELECT("count(1)");
            FROM(meta.getTableName());
            if(object != null){
                for(ColumnMeta columnMeta : meta.getColumnMetas().values()){
                    if(columnMeta.select()){
                        Object value = columnMeta.get(object);
                        if(value != null){
                            WHERE(columnMeta.getColumnName() + " = ?");
                            parameters.add(new ColumnValueParameter(columnMeta,value));
                        }
                    }
                }
            }
        }}.toString());
        selectQuery.setParameters(parameters);
        selectQuery.setResultSetHandler(new SingleValueResultSetHandler<Integer>(Integer.class));
        return selectQuery;
    }

    @SuppressWarnings("unchecked")
    protected SelectQuery<?> findOneGenerator(final Object object){
        final ColumnMeta idColumnMeta = meta.getIdColumn();
        if(object == null){
            throw new IllegalArgumentException("cannot find while id is null.");
        }
        SelectQuery<?> query = new SelectQuery<Object>();
        query.setSql(
                query().WHERE(meta.getIdColumn().getColumnName() + " = ?").toString()
        );
        query.setResultSetHandler(resultSetHandler);
        query.setParameters(Collections.<Parameter>singletonList(new ColumnValueParameter(idColumnMeta,object)));
        return query;
    }

    @SuppressWarnings("unchecked")
    protected SelectQuery<?> findAllGenerator(){
        SelectQuery<?> query = new SelectQuery<Object>();
        query.setSql(query().toString());
        query.setResultSetHandler(resultSetHandler);
        query.setParameters(Collections.<Parameter>emptyList());
        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SelectQuery<?> findAllQuery(QueryWrapper queryWrapper) {
        SelectQuery query;
        SQL sql;
        if(queryWrapper.getEntity() == null){
            query = new SelectQuery<Object>();
            sql = query();
            query.setParameters(new ArrayParameters(queryWrapper.getParameters().toArray(),norm.getJdbcNullType()));
        }else{
            query = findAllFilterGenerator(queryWrapper.getEntity());
            sql = ((SelectQueryMiddle)query).getSqlBuilder();
            List<Parameter> parameters = new ArrayList<Parameter>(((List<Parameter>) query.getParameters()));
            ArrayParameters arrayParameters = new ArrayParameters(queryWrapper.getParameters().toArray(),norm.getJdbcNullType());
            for(Parameter parameter : arrayParameters){
                parameters.add(parameter);
            }
            query.setParameters(parameters);
        }
        queryWrapper.eval(sql);
        query.setResultSetHandler(resultSetHandler);
        query.setSql(sql.toString());
        return query;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @SuppressWarnings("unchecked")
    protected SelectQueryMiddle<?> findAllFilterGenerator(final Object object){
        final List<Parameter> parameters = new ArrayList<Parameter>();
        SQL sql = query();
        for(ColumnMeta column : meta.getColumnMetas().values()){
            if(column.select() && !column.isJoinColumn()){
                Object value = column.get(object);
                if(value != null){
                    sql.WHERE(column.getColumnName() + " = ?");
                    parameters.add(new ColumnPropertyParameter(column,object));
                }
            }
        }
        SelectQueryMiddle<Object> query = new SelectQueryMiddle<Object>(sql);
        query.setParameters(parameters);
        query.setResultSetHandler(resultSetHandler);
        return query;
    }

    protected SQL query(){
        return new SQL(){{
            for(ColumnMeta columnMeta : meta.getColumnMetas().values()){
                if(columnMeta.select()){
                    SELECT(columnMeta.getColumnName());
                }
            }
            FROM(meta.getTableName());
            OrderBy orderBy = (OrderBy) meta.getAnnotation(OrderBy.class);
            if(orderBy != null){
                ORDER_BY(orderBy.value() + " " + orderBy.type().name());
            }
            OrderBys orderBys = meta.getAnnotation(OrderBys.class);
            if(orderBys != null){
                for(OrderBy o : orderBys.value()){
                    ORDER_BY(o.value() + " " + o.type().name());
                }
            }
        }};
    }

    protected ResultSetHandler<?> generateResultSetHandler(){
        return new CrudResultSetHandler(meta);
    }



    @Override
    public ResultSetHandler<?> getResultSetHandler() {
        return resultSetHandler;
    }

    public void setResultSetHandler(ResultSetHandler<?> resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
    }
}
