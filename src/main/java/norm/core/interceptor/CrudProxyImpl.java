package norm.core.interceptor;

import norm.Configuration;
import norm.JdbcTemplate;
import norm.Norm;
import norm.QueryWrapper;
import norm.core.executor.Executor;
import norm.core.generator.GeneratorIds;
import norm.core.generator.QueryGenerator;
import norm.core.query.SelectQuery;
import norm.core.query.UpdateQuery;
import norm.exception.ExecutorException;
import norm.page.Page;
import norm.util.AssertUtils;
import norm.util.ErrorContext;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CrudProxyImpl implements CrudProxy {

    private final Norm norm;
    private final QueryGenerator generator;
    private final Executor executor;
    private final JdbcTemplate<Object> jdbcTemplate;

    public CrudProxyImpl(Norm norm, QueryGenerator generator) {
        this.norm = norm;
        this.generator = generator;
        this.executor = norm.getConfiguration().getExecutorFactory().getExecutor(norm);
        this.jdbcTemplate = new JdbcTemplateImpl<Object>(this);
    }

    @Override
    public Norm getNorm() {
        return norm;
    }

    public Configuration getConfiguration() {
        return norm.getConfiguration();
    }
    @Override
    public QueryGenerator getGenerator() {
        return generator;
    }

    @Override
    public Connection openConnection() throws SQLException {
        return norm.openConnection();
    }

    public final boolean generateAndUpdate(String id, Object object){
        UpdateQuery query = generator.update(id,object);
        return executeUpdate(query) > 0;
    }
    @Override
    public int executeUpdate(UpdateQuery updateQuery){
        Connection connection = null;
        try{
            connection = openConnection();
            return executor.executeUpdate(connection,updateQuery);
        }catch (Exception e){
            throw norm.handleError(e);
        }finally {
            ErrorContext.clear();
            norm.releaseConnection(connection);
        }
    }
    @Override
    public <T> T selectOne(SelectQuery<T> query){
        Connection connection = null;
        try{
            connection = openConnection();
            return executor.selectOne(connection,query);
        }catch (Exception e){
            throw norm.handleError(e);
        }finally {
            ErrorContext.clear();
            norm.releaseConnection(connection);
        }
    }
    @Override
    public <T> List<T> selectList(SelectQuery<T> query){
        Connection connection = null;
        try{
            connection = openConnection();
            return executor.selectList(connection,query);
        }catch (Exception e){
            throw norm.handleError(e);
        }finally {
            ErrorContext.clear();
            norm.releaseConnection(connection);
        }
    }
    @Override
    public <T> List<T> selectPage(SelectQuery<T> query,Page<T> page){
        Connection connection = null;
        try{
            if(norm.getPageSql() == null){
                throw new ExecutorException("the pageSql of norm has not configured.");
            }
            connection = openConnection();
            executor.processPage(connection,query,page);
            List<T> list = executor.selectList(connection,query);
            page.setRecords(list);
            return list;
        }catch (Exception e){
            throw norm.handleError(e);
        }finally {
            ErrorContext.clear();
            norm.releaseConnection(connection);
        }
    }
    @Override
    public boolean save(Object o) {
        AssertUtils.notNull(o,"save object");
        return generateAndUpdate(GeneratorIds.SAVE,o);
    }

    @Override
    public boolean delete(Object o) {
        AssertUtils.notNull(o,"delete object");
        return generateAndUpdate(GeneratorIds.DELETE,o);
    }

    @Override
    public boolean deleteByID(Object o) {
        AssertUtils.notNull(o,"delete object");
        return generateAndUpdate(GeneratorIds.DELETE_BY_ID,o);
    }


    @Override
    public boolean update(Object o) {
        AssertUtils.notNull(o,"update object");
        return generateAndUpdate(GeneratorIds.UPDATE,o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean exists(Object o) {
        AssertUtils.notNull(o,"exists object");
        SelectQuery<Integer> query = (SelectQuery<Integer>) generator.select(GeneratorIds.EXISTS,o);
        return selectOne(query) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int count(Object o) {
        AssertUtils.notNull(o,"the object");
        SelectQuery<Integer> query = (SelectQuery<Integer>) generator.select(GeneratorIds.COUNT,o);
        return selectOne(query);
    }

    @Override
    public Object findOne(Object o) {
        AssertUtils.notNull(o,"select object");
        SelectQuery<?> query = generator.select(GeneratorIds.FIND_ONE,o);
        return selectOne(query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object findOne(QueryWrapper queryWrapper) {
        AssertUtils.notNull(queryWrapper,"the QueryWrapper");
        SelectQuery<Object> query = (SelectQuery<Object>) generator.findAllQuery(queryWrapper);
        return selectOne(query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> findAll() {
        SelectQuery<Object> query = (SelectQuery<Object>) generator.select(GeneratorIds.FIND_ALL,null);
        return selectList(query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> findAll(Page page) {
        AssertUtils.notNull(page,"page object");
        SelectQuery<Object> query = (SelectQuery<Object>) generator.select(GeneratorIds.FIND_ALL,null);
        return selectPage(query,page);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> findAll(Object o) {
        AssertUtils.notNull(o,"the object");
        SelectQuery<Object> query = (SelectQuery<Object>) generator.select(GeneratorIds.FIND_ALL,o);
        return selectList(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> findAll(QueryWrapper queryWrapper, Page<Object> page) {
        AssertUtils.notNull(queryWrapper,"the QueryWrapper");
        AssertUtils.notNull(page,"the page");
        SelectQuery<Object> query = (SelectQuery<Object>) generator.findAllQuery(queryWrapper);
        return selectPage(query,page);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> findAll(QueryWrapper queryWrapper) {
        AssertUtils.notNull(queryWrapper,"the QueryWrapper");
        SelectQuery<Object> query = (SelectQuery<Object>) generator.findAllQuery(queryWrapper);
        return selectList(query);
    }

    @Override
    public JdbcTemplate<Object> getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> findAll(Object o, Page page) {
        AssertUtils.notNull(o,"the object");
        AssertUtils.notNull(page,"page object");
        SelectQuery<Object> query = (SelectQuery<Object>) generator.select(GeneratorIds.FIND_ALL,o);
        return selectPage(query,page);
    }
}
