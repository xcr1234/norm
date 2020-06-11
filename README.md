# Norm - 一个Java的ORM框架

Norm是一套微型的JAVA数据库ORM库，提供了简单高效的 API，仅需一个600KB左右的JAR包。 让开发者不需要关心数据库操作的具体细节，只需专注SQL和业务逻辑。同时，也提供了对于事务、缓存是处理，支持在Spring环境中运行！

Norm的设计参考了Hibernate、Spring Data Jpa、Sqlla、DbUtils等数据库框架，它吸收了这些框架的优点，同时解决了一些开发过程中遇到的问题。它提供了简单的API，让开发者不需要关心数据库操作的具体细节，只需专注SQL和业务逻辑。同时简单的事务模型让开发过程增益很多。.

(Norm = N - ORM = Norm alize) 

 **Norm 1.x版本（最新1.7.2）是作者自用的一个项目，已成功推广到公司项目使用；Norm 2.x版本对代码结构和质量、功能进行了全面重构，更加轻量级，逻辑更加清晰** 

支持版本为JDK1.6，老系统也能使用

## 依赖

2.x稳定版本，maven坐标：

```xml
<dependency>
    <groupId>io.github.xcr1234</groupId>
    <artifactId>norm</artifactId>
    <version>2.4</version>
</dependency>
```


## 简单使用

### 配置

```java
Norm norm = new Norm();
norm.setDriverClass("com.mysql.jdbc.Driver"); 
norm.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
norm.setUsername("root");
norm.setPassword("root");
norm.setShowSql(true);
```

## 创建实体类


```java
package entity;

import norm.anno.Table;
import norm.anno.Id;
import norm.anno.Column;

@Table("tb_user")    //使用 Table 标识后，表名为@Table的值，如果没有 Table 标识，默认是类名）。
public class User {
    @Id
    private Integer id;
    @Column("username")  //使用 Column 标识后的属性使用标识的值来对应表中的列
    private String name;
    public User(){
        //必须有无参数默认构造函数
    }
    //省略get、set、toString方法
}
```

**实体类的@Id和无参数默认构造函数是必须有的，且实体类不可为final。否则会抛出BeanException。** 

## DAO 接口类

```java
package dao;

import norm.CrudDao;
import entity.User;


public interface UserDao extends CrudDao<User,Integer>{

}
```


CrudDao是Norm框架提供的增删改查接口，由于UserDao继承了CrudDao<User,Integer>，因此UserDao就具备了对User实体进行增删改查的功能（Integer是User实体的Id字段的类型）。

 **不用写UserDao 接口的实现类，Norm框架会为你实现。** 

## CRUD查询

```java
UserDao userDao = norm.createDao(UserDao.class);

//find all 查询
List<User> userList = userDao.findAll();
System.out.println(userList);

 //find all 分页查询，MySQL，查询第2页的内容，每页3条。
norm.setPageSql(new MySQLPage());
Page page = new Page(2,3);
List<User> userListPaged = userDao.findAll(page);
System.out.println(userListPaged);

//find one查询
User user = userDao.findOne(8);
System.out.println(user);

//insert
User myUser = ...;
userDao.save(myUser);

//delete
userDao.delete(user);
userDao.deleteById(1);
```

## Wiki

https://gitee.com/ironV/norm/wikis

