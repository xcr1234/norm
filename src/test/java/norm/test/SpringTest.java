package norm.test;


import norm.test.service.TestService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        TestService testBean = (TestService) context.getBean("testService");

        //这里虽然调用了两次findAll方法，但因为TestService的缓存已经生效，故只会发送一条SQL语句。
        System.out.println(testBean.findAll());
        System.out.println(testBean.findAll());
    }
}
