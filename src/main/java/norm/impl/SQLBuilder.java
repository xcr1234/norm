package norm.impl;


import norm.anno.Column;
import norm.anno.Id;
import norm.anno.OrderBy;
import norm.jdbc.SQL;



/**
 * 构造动态sql的类
 */
public final class SQLBuilder {
    public static String insert(final Meta meta){

        return new SQL(){{
            INSERT_INTO(meta.getTableName());
            for(ColumnMeta columnMeta : meta.getColumnMetas().values()){
                if(columnMeta.insert()){
                    String name = null;
                    Column column = columnMeta.getAnnotation(Column.class);
                    if(column != null && !column.value().isEmpty()){
                        name = column.value();
                    }else{
                        name = columnMeta.getName();
                    }
                    Id id = columnMeta.getAnnotation(Id.class);
                    if(id != null && !id.value().isEmpty()){
                        VALUES(name,id.value());
                    }else{
                        VALUES(name,"?");
                    }

                }
            }
        }}.toString();

    }

    public static String delete(final Meta meta){
        return new SQL(){{
            DELETE_FROM(meta.getTableName());
            WHERE(meta.getIdColumn().getName() + " = ?");
        }}.toString();
    }

    public static String count(Meta meta){
        return "select count(1) from " + meta.getTableName();
    }

    public static String exists(Meta meta){
        return "select 1 from " + meta.getTableName() + " where " + meta.getIdColumn().getName() + " = ?";
    }

    public static String update(final Meta meta){
        return new SQL(){{
            UPDATE(meta.getTableName());
            for(ColumnMeta columnMeta:meta.getColumnMetas().values()){
                if(columnMeta.update()){
                    SET(columnMeta.getName() + " = ?");
                }
            }
            WHERE(meta.getIdColumn().getName() + " = ?");
        }}.toString();
    }

    public static String findById(Meta meta){
        return query(meta).WHERE(meta.getIdColumn().getName() + " = ?").toString();
    }

    public static String findAll(Meta meta){
        return query(meta).toString();
    }

    public static String findByColumn(Meta meta,ColumnMeta columnMeta){
        return query(meta).WHERE(columnMeta.getName() + " = ?").toString();
    }

    public static String deleteAll(Meta meta){
        return "delete from " + meta.getTableName();
    }

    private static SQL query(final Meta meta){
        return new SQL(){{
            for(ColumnMeta columnMeta : meta.getColumnMetas().values()){
                if(columnMeta.select() && !columnMeta.isJoinColumn()){
                    SELECT(columnMeta.getName());
                }
            }
            FROM(meta.getTableName());
            OrderBy orderBy = meta.getAnnotation(OrderBy.class);
            if(orderBy != null){
                if(orderBy.type() == OrderBy.Type.DEFAULT){
                    ORDER_BY(orderBy.value());
                }else{
                    ORDER_BY(orderBy.value() + " " + orderBy.type().name());
                }
            }
        }};
    }


    public static String findAllFilter(Meta meta,Object object){
        if(object == null){
            return findAll(meta);
        }
        SQL sql = query(meta);
        for(ColumnMeta column : meta.getColumnMetas().values()){
            if(column.getAble() && !column.isJoinColumn()){
                Object value = column.get(object);
                if(value != null){
                    sql.WHERE(column.getName() + " = ?");
                }
            }
        }
        return sql.toString();
    }
}
