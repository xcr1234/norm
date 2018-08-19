package org.norm.test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring-executor.xml"})
public abstract class BaseConnTest {

    @Autowired
    private DataSource dataSource;

    protected Connection connection;

    @Before
    public void initConnection() throws SQLException {
        connection = dataSource.getConnection();
    }

    @After
    public void releaseConnection(){
        if(connection != null){
            try{
                connection.close();
            }catch (SQLException e){

            }
        }
    }

}
