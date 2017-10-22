package norm.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 表示外键引用
 *
 * 例子：
 *
 * public class Role{
 *
 *  {@code @Column("userId")}
 *  {@code @Reference(target = "id")}  //表示参考User类下的id属性
 *   private User user;
 * }
 *
 * public class User {
 *
 *  {@code @Id}
 *   private Integer id;
 *
 *  {@code @JoinColumn(target = Role.class,mappedBy = "user")}  //表示参考Role类下的user属性。
 *   private List&lt;Role&gt; roles;
 * }
 *
 *
 *
 * 在数据库表中role.userId参考user.Id。
 *
 * 以{@code @Reference}为注解的列，仍然会被包含在insert/update语句中，
 * 以{@code @JoinColumn}为注解的列，不会被包含在insert/update语句中。
 *
 * 注意target="id"、mappedBy = "user"，这些都指向的是类属性名，而不是表字段名。
 *
 *
 * @deprecated 有bug，慎用，推荐整合mybatis自行查询
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
@Deprecated
public @interface Reference {

    /**
     * 被引用的目标列
     * @return 目标指向的属性名
     */
    String target();

}
