package norm.cache;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class LruCacheManager implements CacheManager{
    private int size;

    private Map<String,Cache0> cacheMap ;


    public LruCacheManager(int size){
        if(size <= 0){
            throw new IllegalArgumentException("cache size <= 0!");
        }
        this.cacheMap = Collections.synchronizedMap(new LinkedHashMap<String, Cache0>());
        this.size = size;
    }

    @Override
    public Cache getCache(String name) {
        Cache0 cache0 = cacheMap.get(name);
        if(cache0 == null){
            LruCache<String,Object> lruCache = new LruCache<String, Object>(size);
            cache0 = new Cache0(lruCache,name);
            cacheMap.put(name,cache0);
        }
        return cache0;
    }


    @Override
    public void evictAll() {
        for(Cache0 cache0 : cacheMap.values()){
            cache0.cache.evictAll();
        }
    }

    private static final class Cache0 implements Cache{

        private LruCache<String,Object> cache;
        private String name;

        public Cache0(LruCache<String, Object> cache, String name) {
            this.cache = cache;
            this.name = name;
        }

        @Override
        public void evictAll() {
            cache.evictAll();
        }

        @Override
        public void evict(String key) {
            cache.remove(key);
        }

        @Override
        public Object get(String key, Class<?> expectedType) {
            return cache.get(key);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void put(String key, Object value) {
            cache.put(key,value);
        }
    }
}
