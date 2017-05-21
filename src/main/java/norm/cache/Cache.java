package norm.cache;

/**
 * 缓存提供器（CacheProvider）接口
 */
public interface Cache {
    /**
     * 清除当前缓存提供器中缓存的内容
     */
    void evictAll();

    /**
     * 移除缓存
     * @param key 缓存的key，不为空
     */
    void evict(String key);

    /**
     * 得到缓存过的对象
     * @param key 缓存的key，不为空
     * @param expectedType 期望得到的对象的类型
     * @return 缓存过的对象，若对应key未被缓存，则返回null.
     */
    Object get(String key, Class<?> expectedType);

    /**
     * 返回当前缓存提供器的名称
     * @return 缓存提供器的名称
     */
    String getName();

    /**
     * 将缓存对象放入缓存
     * @param key 缓存提供器的名称，不为空
     * @param value 待缓存的值，不为空
     */
    void put(String key, Object value);
}
