package norm.impl;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import norm.CrudDao;
import norm.Norm;
import norm.QueryException;

import norm.anno.Id;
import norm.anno.Reference;
import norm.page.Page;
import norm.result.QueryResult;
import norm.util.ResultSetHandler;


import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link CrudDao}接口的实现类，用该类来实现动态代理
 */
public final class CrudDaoImpl implements CrudDao<Object, Object> {

    private static Map<Entry,CrudDaoImpl> entryCrudDaoMap = new ConcurrentHashMap<Entry,CrudDaoImpl>();

    private Meta meta;
    private Norm norm;

    public CrudDaoImpl(Class<?> type, Norm norm) {
        this.norm = norm;
        this.meta = Meta.parse(type, norm.getTableNameStrategy());
    }


    public static CrudDaoImpl create(Class<?> type,Norm norm){
        Entry entry = new Entry(type,norm);
        CrudDaoImpl crudDao = entryCrudDaoMap.get(entry);
        if(crudDao == null){
            crudDao = new CrudDaoImpl(type,norm);
            entryCrudDaoMap.put(entry,crudDao);
        }
        return crudDao;
    }

    private static class Entry{
        Class<?> type;
        Norm norm;
        Entry(Class t,Norm n){this.type = t;this.norm = n;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            if (type != null ? !type.equals(entry.type) : entry.type != null) return false;
            return norm != null ? norm.equals(entry.norm) : entry.norm == null;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (norm != null ? norm.hashCode() : 0);
            return result;
        }
    }

    @Override
    public Object save(Object object) {
        if (object == null) {
            throw new QueryException("cannot save null!");
        }
        String sql = SQLBuilder.insert(meta);
        if (norm.isFormat_sql()) {
            sql = norm.getSqlFormatter().format(sql);
        }
        if (norm.isShow_sql()) {
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        PreparedStatement ps = null;
        if (needIdentity(object)) {
            try {
                ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                for (ColumnMeta columnMeta : meta.getColumnMetas().values()) {
                    if (columnMeta.insert()) {
                        setObject(ps, index++, columnMeta.get(object));
                    }
                }
                int row = ps.executeUpdate();
                if (row <= 0) {
                    return null;
                }
                ResultSet gkRs = ps.getGeneratedKeys();
                if (gkRs.next()) {
                    Object gkId = ResultSetHandler.get(gkRs, 1, meta.getIdColumn().getType(), meta.getIdColumn().toString());
                    meta.getIdColumn().set(object, gkId);
                    return object;
                } else {
                    return object;
                }
            } catch (SQLException e) {
                 handleError(connection,e);
                 return null;
            } finally {
                closeObjects(connection, ps, null);
            }
        } else {
            try {
                ps = connection.prepareStatement(sql);
                int index = 1;
                for (ColumnMeta columnMeta : meta.getColumnMetas().values()) {
                    if (columnMeta.insert()) {
                        setObject(ps, index++, columnMeta.get(object));
                    }
                }
                int row = ps.executeUpdate();
                if(row > 0){
                    return object;
                }
                return null;
            }catch (SQLException e){
                handleError(connection,e);
                return null;
            }finally {
                closeObjects(connection, ps, null);
            }
        }
    }

    @Override
    public boolean delete(Object object) {
        if(object == null){
            throw new QueryException("cannot delete null!");
        }
        String sql = SQLBuilder.delete(meta);
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            setObject(ps,1,meta.getIdColumn().get(object));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            handleError(connection,e);
            return false;
        }finally {
            closeObjects(connection,ps,null);
        }
    }

    @Override
    public boolean deleteByID(Object id) {
        if(id == null){
            throw new QueryException("cannot delete null!");
        }
        String sql = SQLBuilder.delete(meta);
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            setObject(ps,1,id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            handleError(connection,e);
            return false;
        }finally {
            closeObjects(connection,ps,null);
        }
    }

    @Override
    public int deleteAll() {
        String sql = SQLBuilder.deleteAll(meta);
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            return ps.executeUpdate();
        } catch (SQLException e) {
            handleError(connection,e);
            return 0;
        }finally {
            closeObjects(connection,ps,null);
        }
    }

    @Override
    public void update(Object object) {
        if(object == null){
            throw new QueryException("can't update null!");
        }

        String sql = SQLBuilder.update(meta);
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            int count = 1;
            for(ColumnMeta columnMeta:meta.getColumnMetas().values()){
                if(columnMeta.update()){
                    setObject(ps,count ++ ,columnMeta.get(object));
                }
            }
            ps.executeUpdate();
        }catch (SQLException e){
            handleError(connection,e);
        }finally {
            closeObjects(connection,ps,null);
        }
    }

    @Override
    public boolean exists(Object o) {
        String sql = SQLBuilder.exists(meta);
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            handleError(connection,e);
            return false;
        }finally {
            closeObjects(connection,ps,rs);
        }
    }

    @Override
    public int count() {
        String sql = SQLBuilder.count(meta);
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                return 0;
            }
        } catch (SQLException e) {
            handleError(connection,e);
            return 0;
        }finally {
            closeObjects(connection,ps,rs);
        }
    }

    @Override
    public Object findOne(Object id) {
        return findByColumn(id,meta,meta.getIdColumn(),1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> findAll() {
        String sql = SQLBuilder.findAll(meta);
        return listBySql(sql,null,null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> findAll(Page page) {
        String sql = SQLBuilder.findAll(meta);
        return listBySql(sql,page,null);
    }


    Object fromResultSet(Set<String> set,ResultSet rs,Meta meta,int recursion) throws SQLException {

        Object object = createObject(meta);
        for(ColumnMeta cm : meta.getColumnMetas().values()){
            if(set.contains(cm.getName())){
                Reference reference = cm.getAnnotation(Reference.class);
                if(reference != null){
                    if(recursion > norm.getConfiguration().getMaxRecursion()){
                        cm.set(object,null);
                    }else{
                        Meta targetMeta = Meta.parse(cm.getType(),norm.getTableNameStrategy());
                        ColumnMeta targetColumn = targetMeta.getColumnMetas().get(reference.target());
                        Object referenceValue = ResultSetHandler.get(rs,cm.getName(),targetColumn.getType(),cm.toString());
                        Object value = findByColumn(referenceValue,targetMeta,targetColumn,recursion+1);
                        cm.set(object,value);
                    }
                }else{
                    Object value = ResultSetHandler.get(rs,cm.getName(),cm.getType(),cm.toString());
                    cm.set(object,value);
                }
            }
        }

        for(Method method:meta.getAfterInstanceMethods()){
            boolean access = method.isAccessible();
            if(!access){
                method.setAccessible(true);
            }
            try {
                method.invoke(object);
            } catch (Exception e) {
                throw new QueryException("can't invoke @AfterInstance method : " + method+" of " + meta.getClazz(),e);
            }finally {
                if(!access){
                    method.setAccessible(false);
                }
            }
        }
        return object;
    }

    Object findValueBySql(String sql,Object [] params,Class type,String src){
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for(int i=1;i<=params.length;i++){
                setObject(ps,i,params[i-1]);
            }
            rs = ps.executeQuery();

            if(rs.next()){
                return ResultSetHandler.get(rs,1,type,src);
            }

            return null;
        } catch (SQLException e) {
            handleError(connection,e);
            return null;
        }finally {
            closeObjects(connection,ps,rs);
        }
    }

    int updateQuery(String sql,Object [] params){
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for(int i=1;i<=params.length;i++){
                setObject(ps,i,params[i-1]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            handleError(connection,e);
            return 0;
        }finally {
            closeObjects(connection,ps,null);
        }
    }

    Object findBySql(String sql,Object [] params){
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(sql);
            for(int i=1;i<=params.length;i++){
                setObject(ps,i,params[i-1]);
            }
            rs = ps.executeQuery();

            Set<String> set = new HashSet<String>();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for(int i=1;i<=resultSetMetaData.getColumnCount();i++){
                set.add(resultSetMetaData.getColumnLabel(i));
            }

            if(rs.next()){
                return fromResultSet(set,rs,meta,1);
            }

            return null;
        } catch (SQLException e) {
            handleError(connection,e);
            return null;
        }finally {
            closeObjects(connection,ps,rs);
        }
    }


    QueryResult queryResult(String sql, Page pageable, Object [] values){
        if(pageable != null){
            sql = pageable.pageSelect(sql);
        }
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection connection = norm.getConnection();

        try {
            ps = connection.prepareStatement(sql);
            int i = 1;
            if(values != null){
                for(Object value:values){
                    setObject(ps,i++,value);
                }
            }
            if(pageable != null){
                pageable.setState(ps,i);
            }
            rs = ps.executeQuery();
            return new QueryResultImpl(rs,norm.getTableNameStrategy());
        } catch (SQLException e) {
            handleError(connection,e);
            return null;
        }finally {
            closeObjects(connection,ps,rs);
        }
    }

    List listBySql(String sql,Page pageable,Object [] values) {
        if(pageable != null){
            sql = pageable.pageSelect(sql);
        }
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection connection = norm.getConnection();

        try {
            ps = connection.prepareStatement(sql);
            int i = 1;
            if(values != null){
                for(Object value:values){
                    setObject(ps,i++,value);
                }
            }
            if(pageable != null){
                pageable.setState(ps,i);
            }
            rs = ps.executeQuery();
            Set<String> set = new HashSet<String>();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for(int j=1;j<=resultSetMetaData.getColumnCount();j++){
                set.add(resultSetMetaData.getColumnLabel(j));
            }
            List list = new ArrayList();
            while (rs.next()){
                list.add(fromResultSet(set,rs,meta,1));
            }
            return list;
        } catch (SQLException e) {
            handleError(connection,e);
            return null;
        }finally {
            closeObjects(connection,ps,rs);
        }
    }


    private Object createObject(Meta meta){
        if(meta.hasReference()){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(meta.getClazz());
            enhancer.setCallbackFilter(MethodFilter.getInstance());
            enhancer.setCallbacks(new Callback[]{new BeanInterceptor(meta,norm), NoOp.INSTANCE});
            return enhancer.create();
        }
        Class type = meta.getClazz();
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            throw new QueryException("can't instance object of " +type,e);
        } catch (IllegalAccessException e) {
            throw new QueryException("can't instance object of " +type,e);
        }
    }

    private Object findByColumn(Object columnValue,Meta meta,ColumnMeta columnMeta,int recursion){
        String sql = SQLBuilder.findByColumn(meta,columnMeta);
        if(norm.isFormat_sql()){
            sql = norm.getSqlFormatter().format(sql);
        }
        if(norm.isShow_sql()){
            norm.showSQL(sql);
        }
        Connection connection = norm.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(sql);
            setObject(ps,1,columnValue);

            rs = ps.executeQuery();

            Set<String> set = new HashSet<String>();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for(int i=1;i<=resultSetMetaData.getColumnCount();i++){
                set.add(resultSetMetaData.getColumnLabel(i));
            }

            if(rs.next()){

                return fromResultSet(set,rs,meta,recursion);
            }

            return null;
        } catch (SQLException e) {
            handleError(connection,e);
            return null;
        }finally {
            closeObjects(connection,ps,rs);
        }

    }


    private static void setObject(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) {
            ps.setObject(index, null);
        } else if (value instanceof String) {
            ps.setString(index, (String) value);
        } else if (value instanceof Boolean) {
            ps.setBoolean(index, (Boolean) value);
        } else if (value instanceof Byte) {
            ps.setByte(index, (Byte) value);
        } else if (value instanceof Short) {
            ps.setShort(index, (Short) value);
        } else if (value instanceof Integer) {
            ps.setInt(index, (Integer) value);
        } else if (value instanceof Long) {
            ps.setLong(index, (Long) value);
        } else if (value instanceof Float) {
            ps.setFloat(index, (Float) value);
        } else if (value instanceof Double) {
            ps.setDouble(index, (Double) value);
        } else if (value instanceof BigDecimal) {
            ps.setBigDecimal(index, (BigDecimal) value);
        } else if (value instanceof byte[]) {
            ps.setBytes(index, (byte[]) value);
        } else if (value instanceof Timestamp) {
            ps.setTimestamp(index, (Timestamp) value);
        } else if (value instanceof java.sql.Date) {
            ps.setDate(index, (Date) value);
        } else if (value instanceof Time) {
            ps.setTime(index, (Time) value);
        } else if (value instanceof java.util.Date) {
            ps.setDate(index, new java.sql.Date(((java.util.Date) value).getTime()));
        } else if (value instanceof Blob) {
            ps.setBlob(index, (Blob) value);
        } else if (value instanceof InputStream) {
            ps.setBlob(index, (InputStream) value);
        } else if (value instanceof Clob) {
            ps.setClob(index, (Clob) value);
        } else if (value instanceof Reader) {
            ps.setClob(index, (Reader) value);
        } else {
            ps.setObject(index, value);
        }
    }

    private boolean needIdentity(Object object) {
        Id id = meta.getIdColumn().getAnnotation(Id.class);
        return id != null && id.identity() && meta.getIdColumn().get(object) == null;
    }

    private void closeObjects(Connection connection, PreparedStatement ps, ResultSet rs) {
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
        if (connection != null && !norm.getTransactional().hasBegin()) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    private void handleError(Connection connection, SQLException e) {
        if (norm.getTransactional().hasBegin()) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
            } finally {
                try {
                    connection.close();
                } catch (SQLException exx) {
                }
            }
        }
        throw new QueryException(e);
    }


}