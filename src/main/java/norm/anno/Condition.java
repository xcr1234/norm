package norm.anno;

/**
 * 查询时列的条件
 */
public enum Condition {
    /**
     * 等于
     */
    EQ,
    /**
     * 不等于
     */
    NE,
    /**
     * like查询，自动在两边加上%%
     */
    LIKE,
    /**
     * like查询，在左边加上%
     */
    LIKE_LEFT,

    /**
     * like查询，在右边加上%
     */
    LIKE_RIGHT,

    /**
     * like查询，手动加上%或者_
     */
    LIKE_MANUAL,

    /**
     * is null，会忽略value值和nullWhere
     */
    NULL,

    /**
     * is not null，会忽略value值和nullWhere
     */
    NOT_NULL


}
