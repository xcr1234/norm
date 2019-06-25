package org.norm.core.meta;


import org.norm.Configuration;
import org.norm.anno.AfterInstance;
import org.norm.anno.Id;
import org.norm.anno.Table;
import org.norm.anno.Transient;
import org.norm.exception.BeanException;
import org.norm.naming.NameStrategy;
import org.norm.util.Args;
import org.norm.util.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public final class Meta {


    private static Map<Class,Meta> map = Collections.synchronizedMap(new HashMap<Class,Meta>());
    private Configuration configuration;


    private List<Method> afterInstanceMethods = new ArrayList<Method>();


    public static Meta parse(Class clazz,Configuration configuration){
        Args.notNull(clazz,"class");
        Args.notNull(configuration,"configuration");
        Meta meta = map.get(clazz);
        if(meta == null){
            meta = new Meta(clazz,configuration);
            map.put(clazz,meta);
        }
        return meta;
    }


    public List<Method> getAfterInstanceMethods() {
        return afterInstanceMethods;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return (T) clazz.getAnnotation(annotationClass);
    }

    private void init(){
        Method[] methods = clazz.getDeclaredMethods();

        for(Method method:methods){
            if(BeanUtils.isGetter(method)){
                String name = BeanUtils.getEntity(method);
                if(!columnMetas.containsKey(name)){
                    Method setter = null;
                    Field field = null;
                    try{
                        setter = clazz.getDeclaredMethod(BeanUtils.setMethod(name),method.getReturnType());
                    }catch (NoSuchMethodException e){}
                    try {
                        field = clazz.getDeclaredField(name);
                    }catch (NoSuchFieldException e){}
                    ColumnMeta columnMeta = new ColumnMeta(this,field,setter, method,name);
                    if(columnMeta.getAnnotation(Id.class) != null){
                        if(idColumn == null){
                            idColumn = columnMeta;
                        }else if(!idColumn.equals(columnMeta)){
                            throw new BeanException("repeat id field!");
                        }
                    }
                    if(columnMeta.getAnnotation(Transient.class) == null){
                        columnMetas.put(name,columnMeta);
                    }
                }
            }
            if(BeanUtils.isSetter(method)){
                String name = BeanUtils.getEntity(method);
                Method getter = null;
                Field field = null;
                try{
                    getter = clazz.getDeclaredMethod(BeanUtils.getMethod(name,method));
                }catch (Exception e){}
                try {
                    field = clazz.getDeclaredField(name);
                }catch (Exception e){}
                ColumnMeta columnMeta =new ColumnMeta(this,field,method,getter,name);
                if(columnMeta.getAnnotation(Id.class) != null){
                    if(idColumn == null){
                        if(!BeanUtils.isSerializable(columnMeta.getType())){
                            throw new BeanException("id field is not Serializable : "+ clazz);
                        }
                        idColumn = columnMeta;
                    }else if(!idColumn.equals(columnMeta)) {
                        throw new BeanException("repeat id field!");
                    }
                }
                if(columnMeta.getAnnotation(Transient.class) == null){
                    columnMetas.put(name,columnMeta);
                }
            }
            if(method.isAnnotationPresent(AfterInstance.class)){
                if(method.getReturnType() != Void.class && method.getReturnType() != void.class){
                    throw new BeanException("@AfterInstance method " + method + " of " +clazz + " must return void.");
                }
                afterInstanceMethods.add(method);
            }
        }

        if(idColumn == null){
            throw new BeanException("no id field : " + clazz);
        }
        if(idColumn.getAnnotation(Transient.class) != null){
            throw new BeanException("id field can't be Transient :"+clazz);
        }

        Collections.sort(afterInstanceMethods, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                AfterInstance ai1 = o1.getAnnotation(AfterInstance.class);
                AfterInstance ai2 = o2.getAnnotation(AfterInstance.class);
                Integer order1 = ai1 == null ? 0 :ai1.order();
                int order2 = ai2 == null ? 0 : ai2.order();
                return order1.compareTo(order2);
            }
        });

        //check reference.
//        for(ColumnMeta columnMeta : columnMetas.values()){
//            Reference reference = columnMeta.getAnnotation(Reference.class);
//            if(reference != null){
//                if(BeanUtils.isBaseClass(columnMeta.getType())){    //基本类型不支持@Reference注解
//                    throw new BeanException("base type doesn't support @Reference :" + columnMeta);
//                }
//                if(!Meta.parse(columnMeta.getType(),configuration).getColumnMetas().containsKey(reference.target())){
//                    throw new BeanException("reference target not found :" + reference.target() + " of "+columnMeta);
//                }
//            }
//            if(columnMeta.getAnnotation(JoinColumn.class) != null){
//                ref = true;
//            }
//        }
    }


    private Meta(Class clazz,Configuration configuration){

        this.clazz = clazz;
        this.configuration = configuration;
        init();

    }

    public NameStrategy getTableNameStrategy() {
        return configuration.getTableNameStrategy();
    }

    private boolean ref = false;

    public boolean hasReference() {
        return ref;
    }

    public boolean hasConstructor(){
        try {
            clazz.getDeclaredConstructor();
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    private ColumnMeta idColumn;
    private Class clazz;
    private Map<String,ColumnMeta> columnMetas = new LinkedHashMap<String,ColumnMeta>();

    public ColumnMeta getIdColumn() {
        return idColumn;
    }

    public Class getClazz() {
        return clazz;
    }

    public Map<String, ColumnMeta> getColumnMetas() {
        return columnMetas;
    }


    public String getTableName() {
        String globalSchema = configuration.getSchema();
        String schema = null;
        String columnName = null;
        if(clazz.isAnnotationPresent(Table.class)){
            Table table = (Table)clazz.getAnnotation(Table.class);
            if(!table.schema().isEmpty()){
                schema = table.schema();
            }
            if(!table.value().isEmpty()){
                columnName = table.value();
            }else{
                columnName = configuration.getTableNameStrategy().format(clazz.getSimpleName());
            }
        }else{
            columnName = configuration.getTableNameStrategy().format(clazz.getSimpleName());
        }
        if(globalSchema == null && schema == null){
            return columnName;
        }else if(schema != null){
            return schema + "." + columnName;
        }else{
            return globalSchema + "." + columnName;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Meta meta = (Meta) object;

        return clazz != null ? clazz.equals(meta.clazz) : meta.clazz == null;
    }

    @Override
    public int hashCode() {
        return clazz != null ? clazz.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "idColumn=" + idColumn +
                ", clazz=" + clazz +
                ", columnMetas=" + columnMetas +
                ", tableName=" + getTableName() +
                '}';
    }



}