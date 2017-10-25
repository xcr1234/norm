package norm.support.mybatis;

import net.sf.cglib.proxy.Enhancer;
import norm.CrudDao;
import norm.Norm;
import norm.NormAware;
import norm.impl.CrudDaoImpl;
import org.mybatis.spring.mapper.MapperFactoryBean;

public class MyMapperFactoryBean<T> extends MapperFactoryBean<T> {

    private Norm norm;

    public Norm getNorm() {
        return norm;
    }

    public void setNorm(Norm norm) {
        this.norm = norm;
    }

    public MyMapperFactoryBean() {
    }

    public MyMapperFactoryBean(Class<T> mapperInterface) {
        super(mapperInterface);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        T object = super.getObject();
        Class mapperInterface = this.getMapperInterface();
        if(!CrudDao.class.isAssignableFrom(mapperInterface)){
            return object;
        }
        CrudDaoImpl dao = norm.createDaoForType(mapperInterface);
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{mapperInterface, NormAware.class,MyBatisMapperAware.class});
        enhancer.setCallback(new MyBatisDaoSupport(object,dao));
        return (T) enhancer.create();
    }
}
