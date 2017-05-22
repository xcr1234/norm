package norm.test;

import norm.Configuration;
import norm.Databases;
import norm.JdbcDrivers;
import norm.Norm;
import norm.NormAware;
import norm.Norms;
import norm.page.Page;
import norm.page.Pages;
import norm.test.dao.UserDao;
import norm.test.entity.User;

import java.sql.Connection;
import java.util.List;


public class Test {
    public static void main(String[] args) {


        //三种初始化方式：

        //①声明Configuration对象
        //Configuration configuration = new Configuration(JdbcDrivers.MYSQL,"jdbc:mysql://localhost:3306/test","root","root");
        //Norms.setConfiguration(configuration);

        //②设置全局Norms对象的属性
        Norms.setDriverClass(JdbcDrivers.MYSQL);    // JdbcDrivers.MYSQL =  "com.mysql.jdbc.Driver"
        Norms.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
        Norms.setUsername("root");
        Norms.setPassword("root");
        Norms.setShowSql(true);
        Norms.setFormatSql(true);

        //③通过norm对象的链式语法
        //Norm norm = Norms.getNorm();
        //norm.setDriverClass(JdbcDrivers.MYSQL).setUrl("jdbc:mysql://localhost:3306/test").setUsername("root").setPassword("root");


        UserDao userDao = Norms.createDao(UserDao.class);

        //find all 查询
        List<User> userList = userDao.findAll();

        System.out.println(userList);


        //find all 分页查询，MySQL，查询第2页的内容，每页3条。
        Page page = Pages.create(Databases.MySQL,2,3);

        List<User> userListPaged = userDao.findAll(page);

        System.out.println(userListPaged);

        //获取某一条的roleList。注意，由于roleList是join column类型，所以调用其get方法的时候才会加载（懒加载）
        User user = userDao.findOne(8);
        System.out.println(user.getRoleList());


        //关于Norm、Norms、Page、Pages四个对象
        //这四个对象是Norm框架的核心对象。所有的dao、service、jdbc操作都在Norm对象上进行。分页查询都在Page对象上进行。
        //Norms是一个全局的Norm。相当于 public final static Norm norms = new Norm();
        //Pages是Page的工厂类，所有的Page对象都应该从Pages工厂中获取。
        //因为不同的数据库分页查询sql语句不同，故需要区分Databases：
        //例如：查询第2页的内容，每页10条。
        // Page page = Pages.create(Databases.MySQL,2,10);
        // Page page = Pages.create(Databases.Oracle,2,10);

        //如果系统中没有多个数据库的需求，直接用Norms对象即可。
        //如果系统中连接了多个数据库，那么每个数据库都需要new Norm()，然后设置不同的Configuration。
        //例子：
        //
        //Norm norm1 = new Norm();
        //norm1.setDriverClass("xxx").setUrl().setUsername().setPassword();
        //Norm norm2 = new Norm();
        //norm2.setDriverClass("xxx").setUrl().setUsername().setPassword();


        //所有的Norm dao、Norm service都是实现了NormAware接口的，因此可以方便的取出Norm对象.
        NormAware normAware = (NormAware)userDao;
        Norm norm = normAware.__getNormObject();
        System.out.println(norm.getDriverClass());
        System.out.println(norm.getUsername());

        //事务
        //norm.getTransactional().begin(); //开始事务
        //
        //norm.getTransactional().commit(); //结束事务

        //手动得到Connection（记得close）
        //Connection connection = norm.getConnection();
    }
}
