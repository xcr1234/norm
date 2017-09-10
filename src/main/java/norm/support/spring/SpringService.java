package norm.support.spring;

import norm.CrudDao;
import norm.page.Page;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

public abstract class SpringService<T,ID> implements ApplicationContextAware,InitializingBean{

    private Class<? extends CrudDao<T, ID>>  daoClass;
    private CrudDao<T,ID> dao;

    public SpringService(Class<? extends CrudDao<T, ID>> daoClass) {
        this.daoClass = daoClass;
    }

    protected ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.dao = this.applicationContext.getBean(daoClass);
    }

    @Override
    public final void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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
