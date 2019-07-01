package norm.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 用作java bean的普通方法上，当一个java bean对象被创建，所有数据库查询操作完成后，执行一次该方法。
 * 要求该方法为无参数，且不能是以get/set开头的方法。
 *
 * 不可在构造函数中执行逻辑，因为在触发构造函数时，属性还没有从数据库中被查询出来。
 *
 * 例子：
 *
 * public class ClassA{
 *
 *     private int value;
 *     {@code @Transient }
 *     private int doubleValue;
 *     {@code @AfterInstance }
 *     private void create(){
 *         this.doubleValue = 2 * value;
 *     }
 *     //doubleValue是一个计算值，本身不在数据库字段中。
 * }
 *
 *
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterInstance {
    int order() default 0;
}
