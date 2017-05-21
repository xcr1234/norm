package norm.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主要针对方法配置，能够根据一定的条件对缓存进行清空
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheEvict {
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
     * 是否清空所有缓存内容
     * @return 缺省为 false，如果指定为 true，则方法调用后将立即清空所有缓存
     */
    boolean allEntries() default false;

    /**
     * 是否在方法执行前就清空
     * @return 缺省为 false，如果指定为 true，则在方法还没有执行的时候就清空缓存，缺省情况下，如果方法执行抛出异常，则不会清空缓存
     */
    boolean beforeInvocation() default false;

    /**
     * <pre>
     * 判断是否缓存的表达式，该表达式也是ognl表达式，返回值必须是bool类型。默认为true
     * 方法的参数会以arg0、arg1、arg2、。。。的形式放到ognl context中。如果需要使用参数，也可以使用@Name，为方法的参数命名。
     * </pre>
     * @return 是否缓存的ognl表达式，返回值必须是bool类型。
     */
    String condition() default "";

}
