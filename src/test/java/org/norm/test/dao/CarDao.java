package org.norm.test.dao;

import norm.CrudDao;
import norm.anno.Query;
import org.norm.test.beans.Car;

public interface CarDao extends CrudDao<Car,Integer> {

    @Query(sql = "select * from cars where id > ?")
    Car query(int id);

    //this method not impl
    void query2();

}
