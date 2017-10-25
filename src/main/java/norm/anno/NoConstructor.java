package norm.anno;

import java.lang.annotation.*;

/**
 * 表示在构造一个对象时，不通过构造函数进行构造。
 * 这样，这个类不要求有默认无参的构造方法。
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoConstructor {
}
