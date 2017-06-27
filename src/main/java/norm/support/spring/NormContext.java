package norm.support.spring;



import norm.CrudDao;
import norm.Norm;
import norm.NormAware;
import norm.Norms;
import org.springframework.beans.BeansException;

import org.springframework.beans.factory.config.BeanPostProcessor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * <pre>
 * Norm框架在Spring中的上下文环境
 *
 * 当在Spring中注册了一个NormContext的bean时，spring bean的以下两种自动注入机制将被启用：
 * 1）形如private UserDao userDao;这样的，属性的类型为{@link norm.CrudDao}的子接口，且属性值为null时。自动注入{@link Norm#createDao(Class)}
 * 2）有{@link EnableCache}注解的类型，如
 * {@code @EnableCache}
 * public class TestService {
 * }
 * 在使用TestService时，会自动注入{@link Norm#createService(Class)}。
 *
 *
 * </pre>
 */
public final class NormContext  implements BeanPostProcessor,ApplicationContextAware, NormAware {

    private ApplicationContext applicationContext;
    private Norm norm;

    public Norm getNorm() {
        return norm;
    }

    public void setNorm(Norm norm) {
        this.norm = norm;
    }

    private Norm norm(Field field,Class c ){
        if(field != null && field.isAnnotationPresent(Dao.class)){
            Dao dao = field.getAnnotation(Dao.class);
            if(dao.bean().isEmpty()){
                return norm == null ? Norms.getNorm() : norm;
            }else{
                return applicationContext.getBean(dao.bean(),Norm.class);
            }
        }
        if(c != null && c.isAnnotationPresent(EnableCache.class)){
            EnableCache enableCache = (EnableCache) c.getAnnotation(EnableCache.class);
            if(enableCache.bean().isEmpty()){
                return norm == null ? Norms.getNorm() : norm;
            }else{
                return applicationContext.getBean(enableCache.bean(),Norm.class);
            }
        }
        return norm == null ? Norms.getNorm() : norm;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class cls = bean.getClass();
        for (Field field : cls.getDeclaredFields()) {
            Class c = field.getType();
            if(c.isInterface() && CrudDao.class.isAssignableFrom(c)){
                writeDao(bean,field);
            }
        }
        if(cls.isAnnotationPresent(EnableCache.class)){
            Norm norm = norm(null,cls);
            return norm.enableCache(bean);
        }
        return bean;
    }

    private void writeDao(Object bean,Field field){
        field.setAccessible(true);
        try {
            Object fieldObj = ReflectionUtils.getField(field,bean);
            if(fieldObj != null){
                return;
            }
            Norm norm = norm(field,null);
            Object dao = norm.createDao((Class)field.getType());
            ReflectionUtils.setField(field,bean,dao);
        }finally {
            field.setAccessible(false);
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Norm __getNormObject() {
        return norm == null ? Norms.getNorm() : norm;
    }
}
