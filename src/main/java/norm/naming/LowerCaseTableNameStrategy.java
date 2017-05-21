package norm.naming;

/**
 * 小写命名策略，表名就是类名的小写形式。
 */
public class LowerCaseTableNameStrategy implements TableNameStrategy{
    private static final long serialVersionUID = 9013561155625745756L;

    @Override
    public String format(String className) {
        return className.toLowerCase();
    }
}
