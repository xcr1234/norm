package norm.impl;


import norm.anno.Column;
import norm.anno.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造动态sql的类
 */
public final class SQLBuilder {
    public static String insert(Meta meta){
        StringBuilder sb = new StringBuilder(32);
        sb.append("insert into ").append(meta.getTableName()).append(" (");
        int count = 0;
        List<ColumnMeta> columnMetaList = new ArrayList<ColumnMeta>();
        for(ColumnMeta columnMeta : meta.getColumnMetas().values()){
            if(columnMeta.insert()){
                String name = null;
                Column column = columnMeta.getAnnotation(Column.class);
                columnMetaList.add(columnMeta);
                if(column != null && !column.value().isEmpty()){
                    name = column.value();
                }else{
                    name = columnMeta.getName();
                }
                if(count > 0){
                    sb.append(',');
                }
                sb.append(name);
                count++;
            }
        }
        sb.append(" ) values ( ");
        for(int i=0;i<count;i++){
            if(i>0){
                sb.append(',');
            }
            Id id = columnMetaList.get(i).getAnnotation(Id.class);
            if(id != null && !id.value().isEmpty()){
                sb.append(id.value());
            }else{
                sb.append('?');
            }
        }
        sb.append(" )");
        return sb.toString();
    }

    public static String delete(Meta meta){
        return "delete from " + meta.getTableName() +
                " where " +
                meta.getIdColumn().getName() +
                " = ?";
    }

    public static String count(Meta meta){
        return "select count(1) from " + meta.getTableName();
    }

    public static String exists(Meta meta){
        return "select 1 from " + meta.getTableName() + " where " + meta.getIdColumn().getName() + " = ?";
    }

    public static String update(Meta meta){
        StringBuilder sb = new StringBuilder(32);
        sb.append("update ").append(meta.getTableName());
        sb.append(" set ");
        int count = 0;
        for(ColumnMeta columnMeta:meta.getColumnMetas().values()){
            if(columnMeta.update()){
                if(count > 0) sb.append(',');
                sb.append(columnMeta.getName()).append(" = ? ");
                count++;
            }
        }
        return sb.toString();
    }

    public static String findById(Meta meta){
        return "select * from " + meta.getTableName() + " where "+meta.getIdColumn().getName() + " = ?";
    }

    public static String findAll(Meta meta){
        return "select * from " + meta.getTableName();
    }

    public static String findByColumn(Meta meta,ColumnMeta columnMeta){
        return "select * from " + meta.getTableName() + " where " + columnMeta.getName() + " = ?";
    }

    public static String deleteAll(Meta meta){
        return "delete from " + meta.getTableName();
    }


}
