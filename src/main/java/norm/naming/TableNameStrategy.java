package norm.naming;

import java.io.Serializable;

/**
 * 数据库表的命名策略，定义类名如何向表名转换
 */
public interface TableNameStrategy extends Serializable{
    String format(String className);
}
