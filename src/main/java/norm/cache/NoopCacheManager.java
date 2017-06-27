package norm.cache;


/**
 * 不缓存任何内容
 */
public final class NoopCacheManager implements CacheManager{

    private static final NoopCacheManager noopCacheManager = new NoopCacheManager();

    public static NoopCacheManager getInstance(){
        return noopCacheManager;
    }


    @Override
    public Cache getCache(String name) {
        return null;
    }

    @Override
    public void evictAll() {

    }
}
