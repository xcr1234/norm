package norm;

import norm.page.Page;

import java.util.List;

public abstract class Service<T, ID> implements BaseService<T, ID> {


    protected CrudDao<T, ID> dao;

    protected Service(CrudDao<T, ID> dao) {
        this.dao = dao;
    }


    public Service(Class<? extends CrudDao<T, ID>> daoClass, Norm norm) {
        this.dao = norm.createDao(daoClass);
    }
    @Override
    public boolean save(T t) {
        return dao.save(t);
    }
    @Override
    public boolean delete(T t) {
        return dao.delete(t);
    }
    @Override
    public boolean deleteByID(ID id) {
        return dao.deleteByID(id);
    }
    @Override
    public int deleteAll() {
        return dao.deleteAll();
    }
    @Override
    public void update(T t) {
        dao.update(t);
    }
    @Override
    public boolean exists(ID id) {
        return dao.exists(id);
    }
    @Override
    public int count(Object o) {
        return dao.count(o);
    }
    @Override
    public T findOne(ID id) {
        return dao.findOne(id);
    }
    @Override
    public List<T> findAll() {
        return dao.findAll();
    }
    @Override
    public List<T> findAll(Page page) {
        return dao.findAll(page);
    }

    @Override
    public List<T> findAll(T t) {
        return dao.findAll(t);
    }

    @Override
    public List<T> findAll(T t, Page page) {
        return dao.findAll(t, page);
    }

    @Override
    public List<T> findAll(QueryWrapper queryWrapper, Page<T> page) {
        return dao.findAll(queryWrapper, page);
    }

    @Override
    public List<T> findAll(QueryWrapper queryWrapper) {
        return dao.findAll(queryWrapper);
    }
}
