package org.sun.bright.framework.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sun.bright.framework.cache.enums.CacheAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> caffeineCaches = new ArrayList<>();
        for (CacheAttribute cacheAttribute : CacheAttribute.values()) {
            caffeineCaches.add(new CaffeineCache(cacheAttribute.name(),
                    Caffeine.newBuilder()
                            // 设置最后一次写入或访问后经过固定时间过期
                            .expireAfterWrite(cacheAttribute.getExpires(), TimeUnit.SECONDS)
                            // 初始的缓存空间大小
                            .initialCapacity(cacheAttribute.getInitNum())
                            // 缓存的最大条数
                            .maximumSize(cacheAttribute.getMaxNum())
                            .build()));
        }
        cacheManager.setCaches(caffeineCaches);
        return cacheManager;
    }

}
