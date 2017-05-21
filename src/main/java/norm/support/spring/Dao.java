package norm.support.spring;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示这个接口是一个Norm Dao接口，实现类bean将被自动创建。
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dao {
    /**
     * 指向Norm对象的bean名称，默认为全局Norms对象
     * @return bean名称
     */
    String bean() default "";
}
