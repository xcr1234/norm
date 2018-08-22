package norm.impl;


import norm.*;

import norm.anno.Id;

import norm.anno.NoConstructor;
import norm.page.Page;
import norm.result.QueryResult;
import norm.util.ResultSetHandler;


import sun.reflect.ReflectionFactory;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.AccessController;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link CrudDao}接口的实现类，用该类来实现动态代理
 */
public final class CrudDaoImpl implements CrudDao<Object, Object>, NormAware {

    private static Map<Entry,CrudDaoImpl> entryCrudDaoMap = new ConcurrentHashMap<Entry,CrudDaoImpl>();

    private Meta meta;
    private Norm norm;

    private CrudDaoImpl(Class<?> type, Norm norm) {
        this.norm = norm;
        this.meta = Meta.parse(type, norm.getConfiguration());
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

    @Override
    public Norm __getNormObject() {
        return norm;
    }

    public Meta getMeta() {
        return meta;
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
        List<Object> argList = new ArrayList<Object>();
        if (needIdentity(object)) {
            try {
                ps = norm.isCollectGenerateId() ? connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : connection.prepareStatement(sql);
                int index = 1;
                for (ColumnMeta columnMeta : meta.getColumnMetas().values()) {
                    Id id = columnMeta.getAnnotation(Id.class);
                    if (columnMeta.insert() && (id == null || id.value().isEmpty())) {
                        //踢出id value的情况
                        Object value = columnMeta.get(object);
                        argList.add(value);
                        setObject(ps, index++, value);
                    }
                }
                int row = ps.executeUpdate();
                if (row <= 0) {
                    return null;
                }
                if(norm.isCollectGenerateId()){
                    ResultSet gkRs = ps.getGeneratedKeys();
                    ColumnMeta idColumn = meta.getIdColumn();
                    if (gkRs.next()) {
                        Object gkId = ResultSetHandler.get(gkRs, 1, meta.getIdColumn().getType(), meta.getIdColumn().toString());
                        idColumn.set(object, gkId);
                        return object;
                    }
                }
                return object;
            } catch (Exception e) {
                 handleError(connection,e,sql,argList);
                 return null;
            } finally {
                closeObjects(connection, ps, null);
            }
        } else {
            try {
                ps = connection.prepareStatement(sql);
                int index = 1;
                for (ColumnMeta columnMeta : meta.getColumnMetas().values()) {
                    Id id = columnMeta.getAnnotation(Id.class);
                    if (columnMeta.insert() && (id == null || id.value().isEmpty())) {
                        Object value = columnMeta.get(object);
                        argList.add(value);
                        setObject(ps, index++, value);
                    }
                }
                int row = ps.executeUpdate();
                if(row > 0){
                    return object;
                }
                return null;
            }catch (Exception e){
                handleError(connection,e,sql,argList);
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
        List<Object> argList = new ArrayList<Object>();
        try {
            ps = connection.prepareStatement(sql);
            Object value = meta.getIdColumn().get(object);
            argList.add(value);
            setObject(ps,1,value);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            handleError(connection,e,sql,argList);
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
            handleError(connection,e,sql,Collections.singletonList(id));
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
            handleError(connection,e,sql);
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
        List<Object> argList = new ArrayList<Object>();
        try {
            Object idValue = meta.getIdColumn().get(object);
            if(idValue == null){
                throw new SQLException("can't update while id is null!");
            }
            ps = connection.prepareStatement(sql);
            int count = 1;
            for(ColumnMeta columnMeta:meta.getColumnMetas().values()){
                if(columnMeta.update()){
                    Object value = columnMeta.get(object);
                    argList.add(value);
                    setObject(ps,count ++ ,value);
                }
            }
            argList.add(idValue);
            setObject(ps,count,idValue);
            ps.executeUpdate();
        }catch (SQLException e){
            handleError(connection,e,sql,argList);
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
        Object idValue = null;
        try {
            idValue = meta.getIdColumn().get(o);
            if(idValue == null){
                throw new SQLException("can't exists while id is null!");
            }
            ps = connection.prepareStatement(sql);
            setObject(ps,1,idValue);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            handleError(connection,e,sql,Collections.singletonList(idValue));
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
            handleError(connection,e,sql);
            return 0;
        }finally {
            closeObjects(connection,ps,rs);
        }
    }

    @Override
    public Object findOne(Object id) {
        return findByColumn(id,meta,meta.getIdColumn(),1);
    }

    @Override
    public List<Object> findAll() {
        return findAll((Page)null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> findAll(Page page) {
        String sql = SQLBuilder.findAll(meta);
        return listBySql(sql,page,null);
    }

    @Override
    public List<Object> findAll(Object o) {
        return findAll(o,null);
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<Object> findAll(Object o, Page page) {
        String sql = SQLBuilder.findAllFilter(meta,o);
        ArrayList<Object> list = new ArrayList<Object>();
        for(ColumnMeta column : meta.getColumnMetas().values()){
            if(column.getAble()){
                Object value = column.get(o);
                if(value != null){
                    list.add(value);
                }
            }
        }
        return listBySql(sql,page,list.toArray());
    }

    @SuppressWarnings("deprecation")
    private Object fromResultSet(Set<String> set,ResultSet rs,Meta meta,int recursion) throws SQLException {

        Object object = createObject(meta);
        for(ColumnMeta cm : meta.getColumnMetas().values()){
            if(set.contains(cm.getName()) || set.contains(cm.getName().toUpperCase())){

                Object value = null;
                if(cm.getTypeConverter() == null){
                    value = ResultSetHandler.get(rs,cm.getName(),cm.getType(),cm.toString());
                }else{
                    try{
                        value = cm.getTypeConverter().getObject(rs,cm.getName());
                    }catch (ClassCastException e){
                        throw new ConvertException(cm.getTypeConverter(),null,e);
                    }
                }
                cm.set(object,value);
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
        List<Object> argList = new ArrayList<Object>();
        try {
            ps = connection.prepareStatement(sql);
            for(int i=1;i<=params.length;i++){
                Object value = params[i-1];
                argList.add(value);
                setObject(ps,i,value);
            }
            rs = ps.executeQuery();

            if(rs.next()){
                return ResultSetHandler.get(rs,1,type,src);
            }

            return null;
        } catch (SQLException e) {
            handleError(connection,e,sql,argList);
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
        List<Object> argList = new ArrayList<Object>();
        try {
            ps = connection.prepareStatement(sql);
            for(int i=1;i<=params.length;i++){
                Object value = params[i-1];
                argList.add(value);
                setObject(ps,i,value);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            handleError(connection,e,sql,argList);
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
        List<Object> argList = new ArrayList<Object>();
        try {
            ps = connection.prepareStatement(sql);
            for(int i=1;i<=params.length;i++){
                Object value = params[i-1];
                argList.add(value);
                setObject(ps,i,value);
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
            handleError(connection,e,sql,argList);
            return null;
        }finally {
            closeObjects(connection,ps,rs);
        }
    }


    QueryResult queryResult(String sql, Page pageable, Object [] values){
        if(pageable != null){
            sql = norm.getPageSql().buildSql(pageable,sql);
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
        List<Object> argList = new ArrayList<Object>();
        try {
            ps = connection.prepareStatement(sql);
            int i = 1;
            if(values != null){
                for(Object value:values){
                    argList.add(value);
                    setObject(ps,i++,value);
                }
            }
            rs = ps.executeQuery();
            return new QueryResultImpl(rs,norm.getConfiguration());
        } catch (SQLException e) {
            handleError(connection,e,sql,argList);
            return null;
        }finally {
            closeObjects(connection,ps,rs);
        }
    }

    List listBySql(String originSql,Page pageable,Object [] values) {
        String sql = originSql;
        if(pageable != null){
            sql = norm.getPageSql().buildSql(pageable,sql);
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
        List<Object> argList = new ArrayList<Object>();
        try {
            ps = connection.prepareStatement(sql);
            int i = 1;
            if(values != null){
                for(Object value:values){
                    argList.add(value);
                    setObject(ps,i++,value);
                }
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


            if(pageable != null){
                if(pageable.isEvalCount()){
                    i = 1;
                    String countSql = "select count(1) from ( " + originSql + " ) as rs_";
                    PreparedStatement countPs = null;
                    ResultSet countRs = null;
                    if(norm.isShow_sql()){
                        norm.showSQL(countSql);
                    }
                    List<Object> pageArgList = new ArrayList<Object>();
                    try{
                        countPs = connection.prepareStatement(countSql);
                        if(values != null){
                            for(Object value:values){
                                pageArgList.add(value);
                                setObject(countPs,i++,value);
                            }
                        }
                        countRs = countPs.executeQuery();
                        int totalCount = 0;
                        if(countRs.next()){
                            totalCount = countRs.getInt(1);
                        }
                        pageable.setTotal(totalCount);
                        int pageSize = pageable.getPageSize();
                        pageable.setPageCount(totalCount / pageSize + ((totalCount % pageSize == 0) ? 0 : 1));
                    }catch (SQLException e){
                        handleError(connection,e,countSql,pageArgList);
                        return null;
                    }finally {
                        if(countRs != null){
                            try {
                                countRs.close();
                            } catch (SQLException e) {

                            }
                        }
                        if(countPs != null){
                            try {
                                countPs.close();
                            } catch (SQLException e) {

                            }
                        }
                    }
                }
                if(pageable.isCollectResult()){
                    pageable.setResult(list);
                }
            }
            return list;
        } catch (SQLException e) {
            handleError(connection,e,sql,argList);
            return null;
        }finally {
            closeObjects(connection,ps,rs);
        }
    }



    private static final Constructor<Object> OBJECT_CONSTRUCTOR;

    static {
        try {
            OBJECT_CONSTRUCTOR = Object.class.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new Error("object constructor not found.",e);
        }
    }


    public Object createObject(Meta meta){
        Class type = meta.getClazz();
        if(type.isAnnotationPresent(NoConstructor.class)){
            ReflectionFactory reflectionFactory = (ReflectionFactory) AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());
            Constructor constructor = reflectionFactory.newConstructorForSerialization(type,OBJECT_CONSTRUCTOR);
            try {
                return constructor.newInstance();
            } catch (InstantiationException e) {
                throw new QueryException("can't instance object of " +type + " by @NoConstructor",e);
            } catch (IllegalAccessException e) {
                throw new QueryException("can't instance object of " +type + " by @NoConstructor",e);
            } catch (InvocationTargetException e) {
                throw new QueryException("can't instance object of " +type + " by @NoConstructor",e);
            }
        }
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
            handleError(connection,e,sql,Collections.singletonList(columnValue));
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
        this.norm.close();
    }

    private void handleError(Connection connection, Throwable e,String sql){
        handleError(connection,e,sql,Collections.emptyList());
    }


    private void handleError(Connection connection, Throwable e,String sql,List<?> args) {
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
        QueryException ex = new QueryException("an error occurred while execute jdbc query.\n" +
                "Class:[" + meta.getClazz() + "]\n" +
                "SQL:[" + sql + "]\n" +
                "Parameters:" + showParameters(args),e);
        ex.setType(meta.getClazz());
        ex.setSql(sql);
        ex.setParameters(Collections.unmodifiableList(args));
        ex.setConnection(connection);
        ex.setNorm(norm);
        throw ex;
    }

    private String showParameters(List<?> args){
        if(args == null){
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for(Object arg : args){
            if(arg == null){
                sb.append("(null)");
            }else{
                String clz = arg.getClass().getName();
                if(clz.startsWith("java.lang.")){
                    clz =  arg.getClass().getSimpleName();
                }
                sb.append(arg).append('(').append(clz).append(')');
            }
            if(!first)sb.append(" , ");
            first = false;
        }
        return sb.append(']').toString();
    }



}
