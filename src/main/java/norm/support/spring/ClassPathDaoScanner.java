package norm.support.spring;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import java.util.Arrays;
import java.util.Set;

public class ClassPathDaoScanner extends ClassPathBeanDefinitionScanner {

    private String normBeanName;

    public String getNormBeanName() {
        return normBeanName;
    }

    public void setNormBeanName(String normBeanName) {
        this.normBeanName = normBeanName;
    }

    public ClassPathDaoScanner(BeanDefinitionRegistry registry) {
        super(registry,false);
    }



    public void registerFilters(){
        addIncludeFilter(new NormTypeFilter());
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions  =  super.doScan(basePackages);
        if(beanDefinitions.isEmpty()){
            logger.warn("No Norm dao was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        }else{
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }


    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions){
        for (BeanDefinitionHolder holder : beanDefinitions){
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            if(logger.isDebugEnabled()){
                logger.debug("Creating Dao with name '" + holder.getBeanName()
                        + "' and '" + definition.getBeanClassName() + "' Interface");
            }
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.setBeanClass(DaoFactoryBean.class);
            definition.getPropertyValues().add("norm",new RuntimeBeanReference(this.normBeanName));
        }
    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }


}
