package norm.support.spring;

import norm.anno.cache.Evict;
import norm.anno.cache.EvictAll;
import norm.anno.cache.Put;
import norm.anno.cache.Cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 表示该注解修饰的类，其方法上的cache注解将生效
 *
 * Cache注解：
 * {@link Cache}
 * {@link Evict}
 * {@link EvictAll}
 * {@link Put}
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableCache {
    String bean() default "";
}
