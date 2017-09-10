package norm.test.dao;


import norm.CrudDao;
import norm.test.entity.User;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends CrudDao<User,Integer>{

}
