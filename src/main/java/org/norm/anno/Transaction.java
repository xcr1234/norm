package org.norm.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 用在Service类的方法中。
 * 当一个方法被加上@Transaction注解后，在方法执行前会开启一个事务，在方法执行结束后该事务
 * 如果方法执行过程中出现了异常，事务会自动被rollback（回滚）。
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transaction {
    String condition() default "";
}
