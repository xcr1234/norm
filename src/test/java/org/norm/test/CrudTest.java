package org.norm.test;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import norm.Norm;
import norm.QueryWrapper;
import norm.exception.QueryException;
import norm.page.Page;
import norm.page.impl.H2Page;
import org.norm.test.beans.Car;
import org.norm.test.dao.CarDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-executor.xml"})
public class CrudTest extends BaseConnTest {


    private CarDao carDao;

    private static Norm norm;

    @BeforeClass
    public static void initNorm() {
        norm = new Norm();

        norm.setShowSql(true);
        norm.setPageSql(new H2Page());
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        norm.setDataSource(dataSource);
    }

    @Before
    public void init() {

        carDao = norm.createDao(CarDao.class);
    }


    @Test
    public void test1() {
        List<Car> list = carDao.findAll();
        System.out.println(list);
    }


    @Test
    public void test2() {
        Car car = carDao.findOne(1);
        System.out.println(car);
    }


    @Test
    public void testSave() {

        //h2内存数据库 插入后需要在事务中查询
        norm.begin();

        Car car = new Car();
        car.setId(5);
        car.setName("迈巴赫exelero");
        car.setDesc("运动跑车极品");

        System.out.println(carDao.save(car));

        System.out.println(carDao.findOne(5));

        norm.commit();
    }


    @Test
    public void testdeleteByID() {

        norm.begin();

        carDao.deleteByID(2);

        List<Car> list = carDao.findAll();

        System.out.println(list);

        norm.rollback();

        List<Car> list2 = carDao.findAll();

        System.out.println(list2);

        norm.commit();
    }


    @Test
    public void testDelete() {
        norm.begin();

        int rows = carDao.deleteAll();

        System.out.println("delete :" + rows);

        System.out.println("list=" + carDao.findAll());

        norm.rollback();

        System.out.println("list=" + carDao.findAll());

        norm.commit();
    }


    @Test
    public void testUpdate() {
        norm.begin();

        Car car = carDao.findOne(2);
        System.out.println(car);

        car.setName("保时捷macan");
        car.setDesc("保时捷的中型SUV");

        carDao.update(car);

        System.out.println(carDao.findOne(2));

        norm.commit();
    }

    @Test
    public void testExists() {
        System.out.println(carDao.exists(1));
        System.out.println(carDao.exists(5));
    }


    @Test
    public void testCount() {
        System.out.println(carDao.count(null));
    }

    @Test
    public void testFineOne() {
        System.out.println(carDao.findOne(3));
    }

    @Test
    public void testPage() {
        Page<Car> page = new Page<Car>(1, 2);
        List<Car> list = carDao.findAll(page);

        System.out.println(list);
        System.out.println(page.getPageCount());
        System.out.println(page.getTotal());
    }

    @Test(expected = QueryException.class)
    public void testError() {
        try {
            carDao.query(1);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test(expected = NoSuchMethodError.class)
    public void testError2() {
        carDao.query2();
    }

    @Test
    public void testQuery() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.condition( "id", ">", 2)
                .orderBy("id asc");
        List<Car> cars = carDao.findAll(queryWrapper);
        System.out.println(cars);

        queryWrapper = new QueryWrapper();
        queryWrapper.like("descrption", "SUV");
        cars = carDao.findAll(queryWrapper);
        System.out.println(cars);
    }

}
