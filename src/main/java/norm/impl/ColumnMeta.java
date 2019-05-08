package norm.impl;




import norm.BeanException;
import norm.ConvertException;
import norm.QueryException;
import norm.anno.AfterInstance;
import norm.anno.Column;
import norm.anno.Converter;
import norm.anno.Id;
import norm.convert.TypeConverter;
//import norm.anno.JoinColumn;
//import norm.anno.Reference;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class ColumnMeta {

    private Field field;
    private Method setter;
    private Method getter;
    private String name;
    private Meta meta;
    private TypeConverter typeConverter;



    public ColumnMeta(Meta meta,Field field, Method setter, Method getter,String name) {
        assert meta != null && name != null && (field != null || setter != null || getter != null);
        this.meta = meta;
        this.field = field;
        this.setter = setter;
        this.getter = getter;
        this.name = name;

        Converter converter = getAnnotation(Converter.class);
        if(converter != null){
            try {
                typeConverter = converter.value().newInstance();
                typeConverter.init(converter.init());
            } catch (Exception e) {
                throw new BeanException("can't create converter for column : [" + this.toString() + "]",e);
            }
        }

    }


    public Class getType(){
        if(getter != null){
            return getter.getReturnType();
        }else if(field != null){
            return field.getType();
        }else if(setter != null){
            return setter.getParameterTypes()[0];
        }
        return null;
    }

    public  <T extends Annotation> T getAnnotation(Class<T> type) {
        if (getter != null && getter.isAnnotationPresent(type)) {
            return getter.getAnnotation(type);
        } else if (field != null && field.isAnnotationPresent(type)) {
            return field.getAnnotation(type);
        } else if (setter != null && setter.isAnnotationPresent(type)) {
            return setter.getAnnotation(type);
        }
        return null;
    }

    public String getName() {
        Column column = getAnnotation(Column.class);
        if (column != null && !"".equals(column.value())) {
            return column.value();
        }
        return meta.getConfiguration().getColumnNameStrategy().format(name);
    }

    public boolean select(){
        if(setter == null){
            return false;
        }
        if(getAnnotation(AfterInstance.class) != null){
            return false;
        }
        Column column = getAnnotation(Column.class);
        return column == null || column.select();
    }

    public boolean insert(){
        if(getter == null){
            return false;
        }
        if(getAnnotation(AfterInstance.class) != null){
            return false;
        }
//        if(getAnnotation(JoinColumn.class) != null){
//            return false;
//        }
        Column column = getAnnotation(Column.class);
        Id id = getAnnotation(Id.class);
        return id == null ? column == null || column.insert() : (!id.identity() || !id.value().isEmpty());
    }

    public boolean update(){
        if(getter == null){
            return false;
        }
        if(getAnnotation(AfterInstance.class) != null){
            return false;
        }
//        if(getAnnotation(JoinColumn.class) != null){
//            return false;
//        }
        Column column = getAnnotation(Column.class);
        return column == null || column.update();
    }

    public Field getField() {
        return field;
    }

    public Method getSetter() {
        return setter;
    }

    public Method getGetter() {
        return getter;
    }

    public boolean getAble(){
        return getter != null;
    }

    public boolean setAble(){
        return setter != null;
    }

    public Object get(Object obj)  {
        try {
            Object value = getter.invoke(obj);
            if(value == null){
                return null;
            }
            if(typeConverter != null){
                try{
                    return typeConverter.setParameter(value);
                }catch (ClassCastException e){
                    throw new ConvertException(typeConverter,value,e);
                }
            }
            return value;
        } catch (IllegalAccessException e) {
            throw new QueryException("can't get the value of "+toString(),e);
        } catch (InvocationTargetException e) {
            throw new QueryException("can't get the value of "+toString(),e);
        }
    }

    public Meta getMeta() {
        return meta;
    }

    public void set(Object object, Object value)  {
        try {
            setter.invoke(object,value);
        } catch (IllegalAccessException e) {
            throw new QueryException("can't get the value of " +toString() ,e);
        } catch (InvocationTargetException e) {
            throw new QueryException("can't get the value of " +toString() ,e);
        }
    }

    @Override
    public String toString() {
        return "column:"+meta.getClazz() + "#"+ name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ColumnMeta that = (ColumnMeta) object;

        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        if (setter != null ? !setter.equals(that.setter) : that.setter != null) return false;
        if (getter != null ? !getter.equals(that.getter) : that.getter != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (setter != null ? setter.hashCode() : 0);
        result = 31 * result + (getter != null ? getter.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public boolean isJoinColumn(){
        return false;
    }

    public boolean isReference(){
        return false;
    }

    public TypeConverter getTypeConverter() {
        return typeConverter;
    }
}