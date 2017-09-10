package norm.test;



import norm.test.service.TestService;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml");

        TestService testService = (TestService) context.getBean("testService");

        System.out.println(testService.findAll());


    }
}
