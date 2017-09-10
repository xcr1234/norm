package norm.support.spring;


import norm.Norm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class NormServiceConfigurer  implements BeanPostProcessor,ApplicationContextAware {

    private Log log = LogFactory.getLog(NormServiceConfigurer.class);

    private ApplicationContext applicationContext;

    private String normBeanName = "norm";

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class cls = bean.getClass();
        if(cls.isAnnotationPresent(EnableCache.class)){
            log.debug("Creating Service with name '" + beanName
                    + "' and '" + cls.getName() + "' Class");
            Norm norm = applicationContext.getBean(normBeanName,Norm.class);
            return norm.enableCache(bean);
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
