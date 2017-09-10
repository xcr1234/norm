package norm;

import norm.page.Page;

import java.util.List;

public abstract class Service<T,ID> {


    private CrudDao<T,ID> dao;


    public Service(Class<? extends CrudDao<T, ID>> daoClass){
        this(daoClass,null);
    }

    public Service(Class<? extends CrudDao<T, ID>> daoClass,Norm norm) {
        if(norm == null){
            norm = Norms.getNorm();
        }
        this.dao = norm.createDao(daoClass);
    }

    public T save(T t) {
        return dao.save(t);
    }

    public boolean delete(T t) {
        return dao.delete(t);
    }

    public boolean deleteByID(ID id) {
        return dao.deleteByID(id);
    }

    public int deleteAll() {
        return dao.deleteAll();
    }

    public void update(T t) {
        dao.update(t);
    }

    public boolean exists(ID id) {
        return dao.exists(id);
    }

    public int count() {
        return dao.count();
    }

    public T findOne(ID id) {
        return dao.findOne(id);
    }

    public List<T> findAll() {
        return dao.findAll();
    }

    public List<T> findAll(Page page) {
        return dao.findAll(page);
    }
}
