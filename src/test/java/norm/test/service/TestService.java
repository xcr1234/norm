package norm.test.service;

import norm.anno.Cacheable;
import norm.support.spring.Dao;
import norm.support.spring.EnableCache;
import norm.test.dao.UserDao;
import norm.test.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableCache
public class TestService {

    @Dao    //这个注解加不加都是可以的，加上是为了说明语义。
    private UserDao userDao;

    @Cacheable(value = "testCache")
    public List<User> findAll(){
        return userDao.findAll();
    }



}
