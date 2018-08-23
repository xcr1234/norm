package org.norm.support.mybatis;

import net.sf.cglib.proxy.Enhancer;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.norm.CrudDao;
import org.norm.Norm;
import org.norm.NormAware;
import org.norm.core.interceptor.CrudDaoImpl;

public class NormMapperFactoryBean<T> extends MapperFactoryBean<T> {


    public NormMapperFactoryBean() {
    }

    public NormMapperFactoryBean(Class<T> mapperInterface) {
        super(mapperInterface);
    }


    private Norm norm;

    public Norm getNorm() {
        return norm;
    }

    public void setNorm(Norm norm) {
        this.norm = norm;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        T t = super.getObject();
        Class mapperInterface = this.getMapperInterface();
        if(!CrudDao.class.isAssignableFrom(mapperInterface)){
            return t;
        }
        CrudDaoImpl dao = norm.createDaoForType(mapperInterface);
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{mapperInterface,MapperAware.class,NormAware.class});
        enhancer.setCallback(new MyBatisMapperSupport(t,dao));
        return (T) enhancer.create();
    }
}

