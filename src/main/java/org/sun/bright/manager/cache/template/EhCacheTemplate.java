package org.sun.bright.manager.cache.template;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Component
public class EhCacheTemplate {

    @Autowired
    private EhCacheCacheManager cacheCacheManager;

    /**
     * 获取缓存
     */
    public Object get(String cacheName, String key) {
        CacheManager cacheManager = cacheCacheManager.getCacheManager();
        if (Objects.nonNull(cacheManager)) {
            Cache cache = cacheManager.getCache(cacheName);
            if (Objects.nonNull(cache)) {
                Element element = cache.get(key);
                return element != null ? element.getObjectValue() : null;
            }
        }
        return null;
    }

    /**
     * set EhCache
     *
     * @param cacheName 底层为Map的key值
     */
    public void put(@NotNull String cacheName, String key, Object value) {
        CacheManager cacheManager = cacheCacheManager.getCacheManager();
        if (Objects.nonNull(cacheManager)) {
            Cache cache = cacheManager.getCache(cacheName);
            if (Objects.nonNull(cache)) {
                Element element = new Element(key, value);
                cache.put(element);
            }
        }
    }

    /**
     * 清空缓存
     */
    public void evict(String cacheName, String key) {
        CacheManager cacheManager = cacheCacheManager.getCacheManager();
        if (Objects.nonNull(cacheManager)) {
            Cache cache = cacheManager.getCache(cacheName);
            if (Objects.nonNull(cache)) {
                cache.remove(key);
            }
        }
    }

}
