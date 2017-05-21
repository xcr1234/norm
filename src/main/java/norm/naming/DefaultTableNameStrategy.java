package norm.naming;

/**
 * 默认的表命名策略，表名就是类名
 */
public class DefaultTableNameStrategy implements TableNameStrategy {

    public static final DefaultTableNameStrategy DEFAULT = new DefaultTableNameStrategy();
    private static final long serialVersionUID = 5124487440007698815L;

    @Override
    public String format(String className) {
        return className;
    }
}
