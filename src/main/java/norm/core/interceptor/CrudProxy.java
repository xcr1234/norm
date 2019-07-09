package norm.core.interceptor;

import norm.CrudDao;
import norm.Norm;
import norm.core.generator.QueryGenerator;
import norm.core.query.SelectQuery;
import norm.core.query.UpdateQuery;
import norm.page.Page;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CrudProxy extends CrudDao<Object, Object> {

    Norm getNorm();

    QueryGenerator getGenerator();

    int executeUpdate(UpdateQuery updateQuery);

    <T> T selectOne(SelectQuery<T> query);

    <T> List<T> selectList(SelectQuery<T> query);

    <T> List<T> selectPage(SelectQuery<T> query, Page<T> page);

    Connection openConnection() throws SQLException;
}
