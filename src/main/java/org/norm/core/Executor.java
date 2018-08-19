package org.norm.core;

import org.norm.Configuration;
import org.norm.page.Page;
import org.norm.util.ErrorContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Executor {

    private Configuration configuration;

    public Executor(Configuration configuration) {
        this.configuration = configuration;
    }

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

    public <T> T selectOne(Connection connection,SelectQuery<T> query) throws SQLException{
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
                throw new SQLDataException("Executor error:selectOne() expected 0 or 1 row,but got least 2 rows.");
            }
            return t;
        }finally {
            closeObjects(ps,rs);
        }
    }

    public <T> List<T> selectList(Connection connection,SelectQuery<T> query) throws SQLException{
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

    public <T> void processPage(Connection connection, SelectQuery<T> query, Page<T> page)throws SQLException{
        String sql = query.getSql();
        if(page.isEvalCount()){
            String countSql = "select count(1) from ( " + sql + " )";
            SelectQuery<Integer> countQuery = new SelectQuery<Integer>();
            countQuery.setSql(countSql);
            countQuery.setParameters(query.getParameters());
            countQuery.setResultSetHandler(new ResultSetHandler<Integer>() {
                @Override
                public Integer handle(ResultSet resultSet) throws SQLException {
                    return resultSet.getInt(1);
                }
            });
            int totalCount = selectOne(connection,countQuery);
            page.setTotal(totalCount);
            int pageSize = page.getPageSize();
            page.setPageCount(totalCount / pageSize + ((totalCount % pageSize == 0) ? 0 : 1));
        }
        query.setSql(configuration.getPageSql().buildSql(page,sql));
    }

    protected PreparedStatement prepareStatement(Connection connection,String sql)throws SQLException{
        ErrorContext errorContext = ErrorContext.instance();
        if(configuration.isShowSql()){
            configuration.getSqlLogger().log(sql);
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
