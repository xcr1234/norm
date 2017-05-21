package norm.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示该属性是数据库的主键字段，主键字段必须有且仅有一个
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Id {
    /**
     * 表示主键字段是否自增，默认为true
     * @return 当自增为true时，主键字段不会出现在insert字段中
     */
    boolean identity() default  true;

    /**
     * 该属性是针对于Oracle数据库，带有序列的自增设计的。当是Oracle数据库使用序列自增时，在这个value中写上序列名.nextval
     * @return 序列名.nextval。例如，序列名是sequence1，那么该值应该是sequence1.nextval
     */
    String value() default "";
}
