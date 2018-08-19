package org.norm.core.interceptor;

import org.norm.Configuration;
import org.norm.CrudDao;
import org.norm.core.*;
import org.norm.page.Page;
import org.norm.util.AssertUtils;
import org.norm.util.ErrorContext;
import org.norm.util.ExceptionUtils;

import java.sql.Connection;
import java.util.List;

public class CrudDaoImpl implements CrudDao<Object, Object> {

    private Configuration configuration;
    private QueryGenerator generator;
    private Executor executor;

    public CrudDaoImpl(Configuration configuration, QueryGenerator generator) {
        this.configuration = configuration;
        this.generator = generator;
        this.executor = new Executor(configuration);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public QueryGenerator getGenerator() {
        return generator;
    }

    private boolean generateAndUpdate(String id, Object object){
        UpdateQuery query = generator.update(id,object);
        return executeUpdate(query) > 0;
    }

    public int executeUpdate(UpdateQuery updateQuery){
        Connection connection = null;
        try{
            connection = configuration.openConnection();
            return executor.executeUpdate(connection,updateQuery);
        }catch (Exception e){
            throw ExceptionUtils.wrap(e);
        }finally {
            ErrorContext.clear();
            configuration.releaseConnection(connection);
        }
    }

    public <T> T selectOne(SelectQuery<T> query){
        Connection connection = null;
        try{
            connection = configuration.openConnection();
            return executor.selectOne(connection,query);
        }catch (Exception e){
            throw ExceptionUtils.wrap(e);
        }finally {
            ErrorContext.clear();
            configuration.releaseConnection(connection);
        }
    }

    public <T> List<T> selectList(SelectQuery<T> query){
        Connection connection = null;
        try{
            connection = configuration.openConnection();
            return executor.selectList(connection,query);
        }catch (Exception e){
            throw ExceptionUtils.wrap(e);
        }finally {
            ErrorContext.clear();
            configuration.releaseConnection(connection);
        }
    }

    public <T> List<T> selectPage(SelectQuery<T> query,Page page){
        Connection connection = null;
        try{
            connection = configuration.openConnection();
            executor.processPage(connection,query,page);
            return executor.selectList(connection,query);
        }catch (Exception e){
            throw ExceptionUtils.wrap(e);
        }finally {
            ErrorContext.clear();
            configuration.releaseConnection(connection);
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
    public int deleteAll() {
        UpdateQuery query = generator.update(GeneratorIds.DELETE_ALL,null);
        return executeUpdate(query);
    }

    @Override
    public void update(Object o) {
        AssertUtils.notNull(o,"update object");
        generateAndUpdate(GeneratorIds.UPDATE,o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean exists(Object o) {
        AssertUtils.notNull(o,"exists object");
        SelectQuery<Boolean> query = (SelectQuery<Boolean>) generator.select(GeneratorIds.EXISTS,o);
        return selectOne(query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int count() {
        SelectQuery<Integer> query = (SelectQuery<Integer>) generator.select(GeneratorIds.COUNT,null);
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
        SelectQuery<Object> query = (SelectQuery<Object>) generator.select(GeneratorIds.FIND_ALL,o);
        return selectList(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> findAll(Object o, Page page) {
        AssertUtils.notNull(page,"page object");
        SelectQuery<Object> query = (SelectQuery<Object>) generator.select(GeneratorIds.FIND_ALL,o);
        return selectPage(query,page);
    }
}
