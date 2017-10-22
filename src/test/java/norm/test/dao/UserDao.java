package norm.test.dao;


import norm.CrudDao;
import norm.test.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserDao extends CrudDao<User,Integer>{

    @Select("select * from user where id = #{0}")
    List<User> listBy(int a);
}
