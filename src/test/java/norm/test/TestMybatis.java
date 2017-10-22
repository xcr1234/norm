package norm.test;

import norm.test.dao.UserDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMybatis {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-mybatis.xml");


        UserDao userDao = context.getBean(UserDao.class);

        System.out.println(userDao.findAll());

        System.out.println(userDao.listBy(1));

    }
}
