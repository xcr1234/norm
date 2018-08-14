package norm.test.service;

import norm.anno.cache.Cache;

import norm.support.spring.EnableCache;
import norm.test.dao.UserDao;
import norm.test.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableCache
public class TestService {

    @Autowired
    private UserDao userDao;

    @Cache(value = "testCache")
    public List<User> findAll(){
        return userDao.findAll();
    }



}
