package norm.anno;

/**
 * 插入或更新实体时字段的策略
 */
public enum FieldStrategy {

    /**
     * 忽略，即始终包含该字段
     */
    IGNORED,
    /**
     * 非空，当值不是null时才会包含该字段
     */
    NOT_NULL,

    /**
     * 非空，当值不是empty时才会包含该字段
     */
    NOT_EMPTY,

    /**
     * 默认策略，即忽略
     */
    DEFAULT;


}
