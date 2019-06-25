package org.norm.support.mybatis;

import java.lang.annotation.*;

/**
 * 声明这个方法由MyBatis执行，norm的Crud方法优先级比MyBatis高，基本crud方法将优先被norm框架执行。
 * 如果确定需要由MyBatis执行Crud方法，则需要使用这个注解。
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokeMyBatis {
}
