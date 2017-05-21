package norm.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主要针对方法配置，能够根据方法的请求参数对其结果进行缓存
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
    /**
     * 缓存的名称
     * @return 缓存的名称
     */
    String value();

    /**
     * <pre>
     * 缓存的key表达式，这里的表达式是ognl表达式。
     * 方法的参数会以arg0、arg1、arg2、。。。的形式放到ognl context中。如果需要使用参数，也可以使用@Name，为方法的参数命名。
     * </pre>
     * @return 缓存的key表达式。默认是方法名
     */
    String key() default "";

    /**
     * <pre>
     * 判断是否缓存的表达式，该表达式也是ognl表达式，返回值必须是bool类型。默认为true
     * 方法的参数会以arg0、arg1、arg2、。。。的形式放到ognl context中。如果需要使用参数，也可以使用@Name，为方法的参数命名。
     * </pre>
     * @return 是否缓存的ognl表达式，返回值必须是bool类型。
     */
    String condition() default "";

}
