package norm.impl;


import norm.BeanException;
import norm.QueryException;
import norm.naming.TableNameStrategy;
import norm.result.QueryResult;
import norm.result.QueryResultItem;
import norm.util.TypeUtils;
import ognl.Ognl;
import ognl.OgnlException;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class QueryResultImpl implements QueryResult,Serializable {


    private static final long serialVersionUID = 1169219576809348723L;
    private List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    private List<Map<Integer,Object>> indexMapList = new ArrayList<Map<Integer, Object>>();
    private List<Map<String,Object>> ognlContexts = new ArrayList<Map<String, Object>>();

    public List<Map<String, Object>> getOgnlContexts() {
        return ognlContexts;
    }

    private TableNameStrategy tableNameStrategy;

    public QueryResultImpl(ResultSet resultSet, TableNameStrategy tableNameStrategy) throws SQLException {
        this.tableNameStrategy = tableNameStrategy;
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        Map<Integer, String> labelIndexMap = new HashMap<Integer, String>();
        for(int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
            String table = resultSetMetaData.getTableName(i);
            if(table.isEmpty()){
                labelIndexMap.put(i,resultSetMetaData.getColumnLabel(i));
            }else {
                labelIndexMap.put(i,resultSetMetaData.getTableName(i) + "." + resultSetMetaData.getColumnLabel(i));
            }
        }

        while (resultSet.next()) {
            Map<Integer,Object> indexMap = new HashMap<Integer,Object>();
            Map<String,Object> maps = new HashMap<String, Object>();
            Map<String,Object> ognlContext = new HashMap<String,Object>();
            for(int i=1;i<=resultSetMetaData.getColumnCount();i++){
                Object value = resultSet.getObject(i);
                indexMap.put(i,value);
                maps.put(labelIndexMap.get(i),value);

                String table = resultSetMetaData.getTableName(i);
                String label = resultSetMetaData.getColumnLabel(i);
                if(table.isEmpty()){
                    ognlContext.put(label,value);
                }else{
                    Map<String,Object> map = (Map<String, Object>) ognlContext.get(table);
                    if(map == null){
                        map = new HashMap<String,Object>();
                        ognlContext.put(table,map);
                    }
                    map.put(label,value);
                }
            }
            ognlContexts.add(ognlContext);
            indexMapList.add(indexMap);
            mapList.add(maps);
        }
    }

    @Override
    public List<QueryResultItem> toList() {
        List<QueryResultItem> list = new ArrayList<QueryResultItem>();
        for(int i=0;i<mapList.size();i++){
            list.add(new QueryResultItemImpl(mapList.get(i),indexMapList.get(i),ognlContexts.get(i)));
        }
        return list;
    }

    private void ensureAfterInstance(Object value){
        //保证AfterInstance方法被执行
        if(value != null){
            try {
                Meta meta = Meta.parse(value.getClass(),tableNameStrategy);
                for(Method after:meta.getAfterInstanceMethods()){
                    try {
                        boolean access = after.isAccessible();
                        if(!access){
                            after.setAccessible(true);
                        }
                        after.invoke(value);
                        if(!access){
                            after.setAccessible(false);
                        }
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                }
            }catch (BeanException e){
            }
        }
    }

    @Override
    public List<?> toOgnlList(String ognl)  {
        List<Object> list = new ArrayList<Object>();
        for(Map<String,Object> ognlContext : ognlContexts){
            try {
                list.add(Ognl.getValue(ognl,ognlContext));
            } catch (OgnlException e) {
                throw new QueryException("invalid ognl expression:" + ognl,e);
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> toMapList() {
        return new ArrayList<Map<String, Object>>(mapList);
    }

    @Override
    public List<Map<Integer,Object>> toIndexMapList(){
        return new ArrayList<Map<Integer, Object>>(indexMapList);
    }

    @Override
    public int size() {
        return mapList.size();
    }

    private final class QueryResultItemImpl implements QueryResultItem,Serializable{

        private static final long serialVersionUID = 1332731470989653783L;
        private Map<String,Object> map;
        private Map<Integer,Object> indexMap;
        private Object ognlContext;

        public QueryResultItemImpl(Map<String, Object> map, Map<Integer, Object> indexMap,Object ognlContext) {
            this.map = map;
            this.indexMap = indexMap;
            this.ognlContext = ognlContext;
        }

        @Override
        public Object getObject(String column) {
            return map.get(column);
        }

        @Override
        public Object getObject(int index) {
            return indexMap.get(index);
        }

        @Override
        public Object getOgnl(String ognl)  {
            Object value = null;
            try {
                value = Ognl.getValue(ognl,ognlContext);
            } catch (OgnlException e) {
                throw new QueryException("invalid ognl expression:" + ognl,e);
            }
            ensureAfterInstance(value);
            return value;
        }

        @Override
        public String getString(int index) {
            Object value = getObject(index);
            return TypeUtils.castToString(value);
        }

        @Override
        public Byte getByte(int index) {
            Object value = getObject(index);
            return TypeUtils.castToByte(value);
        }

        @Override
        public Character getChar(int index) {
            Object value = getObject(index);
            return TypeUtils.castToChar(value);
        }

        @Override
        public Short getShort(int index) {
            Object value = getObject(index);
            return TypeUtils.castToShort(value);
        }

        @Override
        public BigDecimal getBigDecimal(int index) {
            Object value = getObject(index);
            return TypeUtils.castToBigDecimal(value);
        }

        @Override
        public BigInteger getBigInteger(int index) {
            Object value = getObject(index);
            return TypeUtils.castToBigInteger(value);
        }

        @Override
        public Float getFloat(int index) {
            Object value = getObject(index);
            return TypeUtils.castToFloat(value);
        }

        @Override
        public Double getDouble(int index) {
            Object value = getObject(index);
            return TypeUtils.castToDouble(value);
        }

        @Override
        public Date getDate(int index) {
            Object value = getObject(index);
            return TypeUtils.castToDate(value);
        }

        @Override
        public java.sql.Date getSQLDate(int index) {
            Object value = getObject(index);
            return TypeUtils.castToSqlDate(value);
        }

        @Override
        public Timestamp getTimestamp(int index) {
            Object value = getObject(index);
            return TypeUtils.castToTimestamp(value);
        }

        @Override
        public Long getLong(int index) {
            Object value = getObject(index);
            return TypeUtils.castToLong(value);
        }

        @Override
        public Integer getInt(int index) {
            Object value = getObject(index);
            return TypeUtils.castToInt(value);
        }

        @Override
        public byte[] getBytes(int index) {
            Object value = getObject(index);
            return TypeUtils.castToBytes(value);
        }

        @Override
        public Boolean getBoolean(int index) {
            Object value = getObject(index);
            return TypeUtils.castToBoolean(value);
        }

        @Override
        public String getString(String column) {
            Object value = getObject(column);
            return TypeUtils.castToString(value);
        }

        @Override
        public Byte getByte(String column) {
            Object value = getObject(column);
            return TypeUtils.castToByte(value);
        }

        @Override
        public Character getChar(String column) {
            Object value = getObject(column);
            return TypeUtils.castToChar(value);
        }

        @Override
        public Short getShort(String column) {
            Object value = getObject(column);
            return TypeUtils.castToShort(value);
        }

        @Override
        public BigDecimal getBigDecimal(String column) {
            Object value = getObject(column);
            return TypeUtils.castToBigDecimal(value);
        }

        @Override
        public BigInteger getBigInteger(String column) {
            Object value = getObject(column);
            return TypeUtils.castToBigInteger(value);
        }

        @Override
        public Float getFloat(String column) {
            Object value = getObject(column);
            return TypeUtils.castToFloat(value);
        }

        @Override
        public Double getDouble(String column) {
            Object value = getObject(column);
            return TypeUtils.castToDouble(value);
        }

        @Override
        public Date getDate(String column) {
            Object value = getObject(column);
            return TypeUtils.castToDate(value);
        }

        @Override
        public java.sql.Date getSQLDate(String column) {
            Object value = getObject(column);
            return TypeUtils.castToSqlDate(value);
        }

        @Override
        public Timestamp getTimestamp(String column) {
            Object value = getObject(column);
            return TypeUtils.castToTimestamp(value);
        }

        @Override
        public Long getLong(String column) {
            Object value = getObject(column);
            return TypeUtils.castToLong(value);
        }

        @Override
        public Integer getInt(String column) {
            Object value = getObject(column);
            return TypeUtils.castToInt(value);
        }

        @Override
        public byte[] getBytes(String column) {
            Object value = getObject(column);
            return TypeUtils.castToBytes(value);
        }

        @Override
        public Boolean getBoolean(String column) {
            Object value = getObject(column);
            return TypeUtils.castToBoolean(value);
        }

    }



}
