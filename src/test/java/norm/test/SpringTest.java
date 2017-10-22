package norm.test;



import norm.test.entity.User;
import norm.test.service.TestService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;


public class SpringTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml");

        TestService testService = (TestService) context.getBean("testService");

        List<User> userList = testService.findAll();
        System.out.println(userList);
        System.out.println(userList.get(0).getRoleList());
    }
}
