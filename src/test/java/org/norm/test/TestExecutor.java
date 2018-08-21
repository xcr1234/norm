package org.norm.test;

import org.junit.Test;
import org.norm.Norm;
import org.norm.core.executor.Executor;
import org.norm.core.parameter.Parameter;
import org.norm.core.handler.ResultSetHandler;
import org.norm.core.query.SelectQuery;
import org.norm.core.executor.DefaultExecutor;
import org.norm.core.parameter.ValueParameter;
import org.norm.page.Page;
import org.norm.page.impl.H2Page;
import org.norm.test.beans.Car;
import org.norm.util.ExceptionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestExecutor extends BaseConnTest {

    private ResultSetHandler<Car> carHandler = new ResultSetHandler<Car>() {
        @Override
        public Car handle(ResultSet resultSet) throws SQLException {
            Car car = new Car();
            car.setId(resultSet.getInt("id"));
            car.setName(resultSet.getString("name"));
            car.setDesc(resultSet.getString("descrption"));
            return car;
        }
    };




    @Test
    public void test() throws SQLException {
        Norm norm = new Norm();
        norm.setShowSql(true);


        Executor executor = new DefaultExecutor(norm);
        SelectQuery<Car> query = new SelectQuery<Car>();
        query.setSql("select * from cars");
        query.setParameters(Collections.<Parameter>emptyList());
        query.setResultSetHandler(carHandler);
        List<Car> list = executor.selectList(connection,query);
        System.out.println("list = " + list);
    }

    @Test
    public void test2()throws SQLException{
        Norm norm = new Norm();
        norm.setShowSql(true);


        Executor executor = new DefaultExecutor(norm);
        SelectQuery<Car> query = new SelectQuery<Car>();
        query.setSql("select * from cars where id = ? or name like ?");
        query.setParameters(Arrays.asList(new Parameter[]{
                new ValueParameter("id",1),
                new ValueParameter("name","北京现代%")
        }));
        query.setResultSetHandler(carHandler);

        List<Car> list = executor.selectList(connection,query);
        System.out.println(list);
    }

    @Test(expected = SQLException.class)
    public void test3()throws SQLException{
        Norm norm = new Norm();
        norm.setShowSql(true);


        Executor executor = new DefaultExecutor(norm);
        SelectQuery<Car> query = new SelectQuery<Car>();
        query.setSql("select * from cars where id >= ?");
        query.setParameters(Arrays.asList(new Parameter[]{
                new ValueParameter("id",1)
        }));
        query.setResultSetHandler(carHandler);

        try{
            System.out.println(executor.selectOne(connection,query));
        }catch (SQLException e){
            ExceptionUtils.wrap(e).printStackTrace();
            throw e;
        }
    }

    //测试分页查询
    @Test
    public void test4() throws SQLException {
        Norm norm = new Norm();
        norm.setShowSql(true);
        norm.setPageSql(new H2Page());



        Executor executor = new DefaultExecutor(norm);
        SelectQuery<Car> query = new SelectQuery<Car>();
        query.setSql("select * from cars");
        query.setParameters(Collections.<Parameter>emptyList());
        query.setResultSetHandler(carHandler);

        Page<Car> page = new Page<Car>(1,2);  //第一页，每页两条
        executor.processPage(connection,query,page);

        List<Car> list = executor.selectList(connection,query);
        System.out.println("list = " + list);
        System.out.println(page);

        page = new Page<Car>(2,2);
        page.setEvalCount(true);

        query.setSql("select * from cars");
        executor.processPage(connection,query,page);
        list = executor.selectList(connection,query);
        System.out.println("list = " + list);
        System.out.println(page);

        page = new Page<Car>(3,2);
        page.setEvalCount(false);

        query.setSql("select * from cars");
        executor.processPage(connection,query,page);
        list = executor.selectList(connection,query);
        System.out.println("list = " + list);
        System.out.println(page);
    }
}
