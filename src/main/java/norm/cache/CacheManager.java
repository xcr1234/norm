package norm.cache;

import java.util.Collection;

/**
 * 缓存管理器的接口
 */
public interface CacheManager {
    /**
     * 根据缓存提供器（CacheProvider）的名称，得到该缓存提供器的对象。
     * @param name 缓存提供器的名称，不可为空。
     * @return 缓存提供器的实例对象，不可为空
     */
    Cache getCache(String name);


    /**
     * 清除得到当前缓存管理器中，所有缓存提供器的内容
     */
    void evictAll();
}
