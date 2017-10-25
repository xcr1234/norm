package norm.support.spring;

import norm.CrudDao;
import norm.Service;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class SpringService<E extends CrudDao<T,ID>,T,ID> extends Service<T,ID> implements ApplicationContextAware{

    private Class<E> daoClass;

    protected E dao;

    public SpringService(){
        super((CrudDao<T,ID>)null);
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        this.daoClass = (Class<E>) types[0];
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        super.dao = this.dao = applicationContext.getBean(this.daoClass);
    }
}
