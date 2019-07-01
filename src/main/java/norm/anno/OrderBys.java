package norm.anno;

import java.lang.annotation.*;

/**
 * 表示多个{@link OrderBy}查询
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OrderBys {
    OrderBy[] value();
}
