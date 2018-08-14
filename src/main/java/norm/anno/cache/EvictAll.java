package norm.anno.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加上该注解的方法，在执行后CacheManager中的内容会被全部清空
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EvictAll {
    /**
     * 是否在方法执行前就清空
     * @return 缺省为 false，如果指定为 true，则在方法还没有执行的时候就清空缓存，缺省情况下，如果方法执行抛出异常，则不会清空缓存
     */
    boolean beforeInvocation() default false;
}
