package norm.anno;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 表示是一列，当列名称与字段名不同时，使用该注解表示列名。
 * 一列的类型只能是java基本类型、或者java.util.list
 * 该注解可以加在属性、get、set方法上
 * </pre>
 */
@Documented
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * 该属性在数据库中对应的列名，默认为属性名。
     * @return 列名
     */
    String value() default "";

    /**
     * 列是否包含在select语句中，默认为true.
     * @return 是否包含在select语句中
     */
    boolean select() default true;

    /**
     * 列是否包含在insert语句中，默认为true.
     * @return 是否包含在insert语句中
     */
    boolean insert() default true;

    /**
     * 列是否包含在update语句中，默认为true.
     * @return 是否包含在update语句中
     */
    boolean update() default true;

    /**
     * 强制指定列的jdbcType，默认为Integer.MAX_VALUE（自动推断）
     * @return 必须是{@link java.sql.Types}中的常量值
     */
    int jdbcType() default Integer.MAX_VALUE;
}
