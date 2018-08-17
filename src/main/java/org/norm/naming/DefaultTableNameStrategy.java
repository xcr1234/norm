package org.norm.naming;

/**
 * 默认的表命名策略，表名就是类名
 */
public class DefaultTableNameStrategy implements NameStrategy {

    public static final DefaultTableNameStrategy DEFAULT = new DefaultTableNameStrategy();

    @Override
    public String format(String className) {
        return className;
    }
}
