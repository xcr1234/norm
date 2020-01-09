package norm.core.executor;

import norm.Norm;
import norm.core.handler.SingleValueResultSetHandler;
import norm.core.parameter.Parameter;
import norm.core.query.SelectQuery;
import norm.core.query.UpdateQuery;
import norm.exception.ExecutorException;
import norm.page.Page;
import norm.page.PageModel;
import norm.util.ErrorContext;

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
            errorContext.setState(ErrorContext.EXECUTE_UPDATE);
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
            errorContext.setState(ErrorContext.EXECUTE_QUERY);
            rs = ps.executeQuery();
            errorContext.setState(ErrorContext.MAP_OBJECTS);
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
            errorContext.setState(ErrorContext.EXECUTE_QUERY);
            rs = ps.executeQuery();
            errorContext.setState(ErrorContext.MAP_OBJECTS);
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
                ErrorContext.instance().setState(ErrorContext.PAGE_EVAL);
                throw e;
            }catch (RuntimeException ex){
                ErrorContext.instance().setState(ErrorContext.PAGE_EVAL);
                throw ex;
            }
        }
        PageModel pageModel = norm.getPageSql().buildSql(page,sql);
        query.setSql(pageModel.getSql());
        Iterable<Parameter> iterable  = query.getParameters();
        if(iterable != null){
            List<Parameter> list = new ArrayList<Parameter>();
            if(iterable instanceof List){
                list.addAll((List<Parameter>)iterable);
            }else{
                for(Parameter parameter : iterable){
                    list.add(parameter);
                }
            }
            if(pageModel.getFirstParameter() != null){
                list.add(pageModel.getFirstParameter());
            }
            if(pageModel.getSecondParameter() != null){
                list.add(pageModel.getSecondParameter());
            }
            query.setParameters(list);
        }
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
        errorContext.setState(ErrorContext.PREPARE_STATEMENT);
        errorContext.setSql(sql);
        return connection.prepareStatement(sql);
    }

    protected void setParameters(PreparedStatement ps, Iterable<Parameter> parameters) throws SQLException {
        ErrorContext errorContext = ErrorContext.instance();
        errorContext.setState(ErrorContext.SET_PARAMETERS);
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
