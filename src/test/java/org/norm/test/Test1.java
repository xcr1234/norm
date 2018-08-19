package org.norm.test;

import org.junit.Test;
import org.norm.Configuration;
import org.norm.core.Executor;
import org.norm.core.Parameter;
import org.norm.core.ResultSetHandler;
import org.norm.core.SelectQuery;
import org.norm.core.parameter.ValueParameter;
import org.norm.test.beans.Car;
import org.norm.util.ExceptionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test1 extends BaseConnTest {

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
        Configuration configuration = new Configuration();
        configuration.setShowSql(true);


        Executor executor = new Executor(configuration);
        SelectQuery<Car> query = new SelectQuery<Car>();
        query.setSql("select * from cars");
        query.setParameters(Collections.<Parameter>emptyList());
        query.setResultSetHandler(carHandler);
        List<Car> list = executor.selectList(connection,query);
        System.out.println("list = " + list);
    }

    @Test
    public void test2()throws SQLException{
        Configuration configuration = new Configuration();
        configuration.setShowSql(true);


        Executor executor = new Executor(configuration);
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
        Configuration configuration = new Configuration();
        configuration.setShowSql(true);


        Executor executor = new Executor(configuration);
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
}
