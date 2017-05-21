package norm.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 在Cache key拼接表达式时使用，标明当前参数的name，这样就可以在key表达式中使用了
 *
 * 例如：
 * {@code @Cacheable(value = "userCache",key = "'findUserById'+ arg0 + ',' + arg1")}
 *  public User findUserByIdAndName(Integer id,String name)
 *
 * 这里没有用到 {@code @Name} 注解，要获取id，name只能通过arg0，arg1，...的方式获取
 *
 * 如果用到 {@code @Name} 注解：
 *
 * {@code @Cacheable(value = "userCache",key = "'findUserById'+ id + ',' + name")}
 *  public User findUserByIdAndName( {@code @Name}  Integer id, {@code @Name} String name)
 *
 * 注意，key是一个ognl表达式。
 * </pre>
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    String value();
}
