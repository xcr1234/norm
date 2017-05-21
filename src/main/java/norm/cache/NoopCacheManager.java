package norm.cache;


/**
 * 不缓存任何内容
 */
public final class NoopCacheManager implements CacheManager{
    @Override
    public Cache getCache(String name) {
        return null;
    }

    @Override
    public void evictAll() {

    }
}
