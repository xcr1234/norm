package org.norm;

import org.norm.exception.QueryException;
import org.norm.meta.ColumnMeta;
import org.norm.meta.Meta;
import org.norm.result.Parameter;
import org.norm.result.Result;
import org.norm.result.ResultMap;
import org.norm.util.ErrorContext;
import org.norm.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Executor {

    private Configuration configuration;

    public Executor(Configuration configuration) {
        this.configuration = configuration;
    }

    public int executeUpdate(Connection connection, String sql,List<Parameter> params, Object object) throws Exception {
        PreparedStatement ps = null;
        try{
            ps = createStatement(connection,sql);
            fillParams(ps,params,object);
            return ps.executeUpdate();
        }finally {
            ErrorContext.clear();
            closeObjects(ps,null);
        }
    }

    public Object selectOne(Connection connection, ResultMap resultMap, String sql, List<Parameter> params, Object object)throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = createStatement(connection,sql);
            fillParams(ps,params,object);
            rs = ps.executeQuery();
            if(rs.next()){
                return fetchOne(resultMap,rs);
            }else{
                return null;
            }
        }finally {
            ErrorContext.clear();
            closeObjects(ps,rs);
        }
    }

    public List<Object> selectList(Connection connection, ResultMap resultMap, String sql, List<Parameter> params, Object object)throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Object> list = null;
        try{
            ps = createStatement(connection,sql);
            fillParams(ps,params,object);
            rs = ps.executeQuery();
            list = new ArrayList<Object>();;
            while (rs.next()){
                list.add(map(resultMap,rs));
            }
            return list;
        }finally {
            ErrorContext.clear();
            closeObjects(ps,rs);
        }
    }



    protected Object fetchOne(ResultMap resultMap,ResultSet resultSet) throws Exception{
        Object object = map(resultMap,resultSet);
        if(resultSet.next()){
            throw new QueryException("fetchOne() expected 0 or 1 row, but found at least 2 rows.");
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    protected Object map(ResultMap resultMap,ResultSet resultSet)throws Exception{
        ErrorContext errorContext = ErrorContext.getInstance();
        errorContext.setState("map object");
        Meta meta = configuration.getMetaFactory().getMeta(resultMap.getType());
        Object object = meta.newInstance();
        for(Result result : resultMap.getResults()){
            ColumnMeta columnMeta = meta.getColumn(result.getProperty());
            Object value = null;
            TypeConverter<?> converter = result.getConverter();
            if(converter != null){
                value = converter.getObject(resultSet,result.getColumn());
            }else {
                value = JdbcUtils.getObject(resultSet,result.getColumn(),columnMeta.getDeclaringType());
            }
            columnMeta.setValue(object,value);
        }
        return object;
    }

    protected PreparedStatement createStatement(Connection connection,String sql) throws Exception{
        ErrorContext errorContext = ErrorContext.getInstance();
        errorContext.setState("prepare statement");
        errorContext.setSql(sql);
        return connection.prepareStatement(sql);
    }

    @SuppressWarnings("unchecked")
    protected void fillParams(PreparedStatement ps, List<Parameter> params,Object object) throws Exception{
        ErrorContext errorContext = ErrorContext.getInstance();
        errorContext.setState("set parameters");
        Meta meta = configuration.getMetaFactory().getMeta(object);
        int index = 1;
        for(Parameter parameter :params){
            errorContext.setParameter(parameter);
            ColumnMeta columnMeta = meta.getColumn(parameter.getProperty());
            if(columnMeta == null){
                throw new QueryException("property not found : " + parameter.getProperty());
            }
            Object value = columnMeta.getValue(object);
            errorContext.addParam(value);

            TypeConverter converter = parameter.getConverter();
            Integer jdbcType = parameter.getJdbcType();

            if(converter != null){
                converter.setParameter(ps,index,value);
            }else if(value == null){
                ps.setNull(index,configuration.getNullJdbcType());
            }else if(jdbcType != null){
                ps.setObject(index,value,jdbcType);
            }else{
                JdbcUtils.setParameter(ps,index,value);
            }


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
