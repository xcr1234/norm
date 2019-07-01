package norm.support.spring;

import norm.Norm;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;

import java.util.List;
import java.util.StringTokenizer;

public class NormScannerConfigurer implements BeanDefinitionRegistryPostProcessor,ApplicationContextAware {

    private String basePackage;
    private ApplicationContext applicationContext;
    private BeanNameGenerator beanNameGenerator;

    private String normBeanName = "norm";
    private String dataSourceBeanName = "dataSource";


    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getNormBeanName() {
        return normBeanName;
    }

    public void setNormBeanName(String normBeanName) {
        this.normBeanName = normBeanName;
    }

    public BeanNameGenerator getBeanNameGenerator() {
        return beanNameGenerator;
    }

    public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }

    public String getDataSourceBeanName() {
        return dataSourceBeanName;
    }

    public void setDataSourceBeanName(String dataSourceBeanName) {
        this.dataSourceBeanName = dataSourceBeanName;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if(this.basePackage == null ){
            throw new IllegalArgumentException("the package of NormScannerConfigurer is null!");
        }
        if(!registry.containsBeanDefinition(normBeanName)){
            if(registry.containsBeanDefinition(dataSourceBeanName)){
                //动态注册Norm Bean
                registry.registerBeanDefinition(normBeanName, BeanDefinitionBuilder.genericBeanDefinition(Norm.class)
                        .addPropertyReference("dataSource",dataSourceBeanName)
                        .getBeanDefinition());
            }else{
                throw new BeanInitializationException("Neither a Norm bean nor a DataSource bean is defined.");
            }
        }
        ClassPathDaoScanner scanner = new ClassPathDaoScanner(registry);
        scanner.setNormBeanName(normBeanName);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setBeanNameGenerator(this.beanNameGenerator);
        scanner.registerFilters();
        StringTokenizer stringTokenizer = new StringTokenizer(basePackage,",;");
        List<String> list = new ArrayList<String>();
        while (stringTokenizer.hasMoreTokens()){
            list.add(stringTokenizer.nextToken());
        }
        for(String s : list){
            scanner.scan(s);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
