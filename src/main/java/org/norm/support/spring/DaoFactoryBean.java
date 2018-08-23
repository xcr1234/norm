package org.norm.support.spring;

import org.norm.CrudDao;
import org.norm.Norm;
import org.springframework.beans.factory.FactoryBean;

public class DaoFactoryBean<T extends CrudDao> implements FactoryBean<T> {
    public DaoFactoryBean() {
    }

    public DaoFactoryBean(Class<T> type) {
        this.type = type;
    }

    private Class<T> type;
    private Norm norm;

    public Norm getNorm() {
        return norm;
    }

    public void setNorm(Norm norm) {
        this.norm = norm;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    @Override
    public T getObject() throws Exception {
        if(type == null){
            throw new IllegalStateException("the type of DaoFactoryBean is null!");
        }
        return (T) norm.createDao(type);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}