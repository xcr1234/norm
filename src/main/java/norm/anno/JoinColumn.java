package norm.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 加上了该注解的，表示是从另一个实体中引用的List，不会包含在insert/update语句中.
 *
 * 详细的参考说明参阅：{@link Reference}
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface JoinColumn {

    Class target();

    String mappedBy();
}
