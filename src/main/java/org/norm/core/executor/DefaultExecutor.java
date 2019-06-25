package org.norm.core.executor;

import org.norm.Norm;
import org.norm.core.handler.SingleValueResultSetHandler;
import org.norm.core.parameter.Parameter;
import org.norm.core.query.SelectQuery;
import org.norm.core.query.UpdateQuery;
import org.norm.exception.ExecutorException;
import org.norm.page.Page;
import org.norm.util.ErrorContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultExecutor implements Executor {

    private Norm norm;

    public DefaultExecutor(Norm norm) {
        this.norm = norm;
    }

    @Override
    public int executeUpdate(Connection connection, UpdateQuery query)throws SQLException{
        ErrorContext errorContext = ErrorContext.instance();
        PreparedStatement ps = null;
        try{
            ps = prepareStatement(connection,query.getSql());
            setParameters(ps,query.getParameters());
            errorContext.setState("execute update");
            return ps.executeUpdate();
        }finally {
            closeObjects(ps,null);
        }
    }

    @Override
    public <T> T selectOne(Connection connection, SelectQuery<T> query) throws SQLException{
        ErrorContext errorContext = ErrorContext.instance();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = prepareStatement(connection,query.getSql());
            setParameters(ps,query.getParameters());
            errorContext.setState("execute query");
            rs = ps.executeQuery();
            errorContext.setState("map objects");
            if(!rs.next()){
                return null;
            }
            T t = query.getResultSetHandler().handle(rs);
            if(rs.next()){
                throw new ExecutorException("Executor error:selectOne() expected 0 or 1 row , but got least 2 rows.");
            }
            return t;
        }finally {
            closeObjects(ps,rs);
        }
    }

    @Override
    public <T> List<T> selectList(Connection connection, SelectQuery<T> query) throws SQLException{
        ErrorContext errorContext = ErrorContext.instance();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = prepareStatement(connection,query.getSql());
            setParameters(ps,query.getParameters());
            errorContext.setState("execute query");
            rs = ps.executeQuery();
            errorContext.setState("map objects");
            List<T> list = new ArrayList<T>();
            while (rs.next()){
                T t = query.getResultSetHandler().handle(rs);
                list.add(t);
            }
            return list;
        }finally {
            closeObjects(ps,rs);
        }
    }

    protected String countSql(String sql){
        // fix mysql exception
        return "select count(1) from ( " + sql + " ) count_";
    }

    @Override
    public <T> void processPage(Connection connection, SelectQuery<T> query, Page page)throws SQLException{
        String sql = query.getSql();
        if(page.isEvalCount()){
            try{
                String countSql = countSql(sql);
                SelectQuery<Integer> countQuery = new SelectQuery<Integer>();
                countQuery.setSql(countSql);
                countQuery.setParameters(query.getParameters());
                countQuery.setResultSetHandler(new SingleValueResultSetHandler<Integer>(Integer.class));
                int totalCount = selectOne(connection,countQuery);
                page.setTotal(totalCount);
                int pageSize = page.getPageSize();
                page.setPageCount(totalCount / pageSize + ((totalCount % pageSize == 0) ? 0 : 1));
            }catch (SQLException e){
                //当抛出异常时，更改ErrorContext的state
                ErrorContext.instance().setState("page eval count");
                throw e;
            }catch (RuntimeException ex){
                ErrorContext.instance().setState("page eval count");
                throw ex;
            }
        }
        query.setSql(norm.getPageSql().buildSql(page,sql));
    }


    protected PreparedStatement prepareStatement(Connection connection, String sql)throws SQLException{
        ErrorContext errorContext = ErrorContext.instance();
        if(norm.isShowSql()){
            if(norm.getConfiguration().isFormatSql()){
                norm.getSqlLogger().log(norm.getSqlFormatter().format(sql));
            }else{
                norm.getSqlLogger().log(sql);
            }
        }
        errorContext.setState("prepare statement");
        errorContext.setSql(sql);
        return connection.prepareStatement(sql);
    }

    protected void setParameters(PreparedStatement ps, Iterable<Parameter> parameters) throws SQLException {
        ErrorContext errorContext = ErrorContext.instance();
        errorContext.setState("set  parameters");
        int index = 1;
        for(Parameter parameter : parameters){
            errorContext.setParameter(parameter);
            parameter.setParameter(ps,index);
            index++;
        }
        errorContext.setParameter(null);
    }

    private void closeObjects(PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
            }
        }
    }

}
