package org.sun.bright.manager.cache.template;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Cache Template
 * 通过 Spring annotation {@link Component} 声明为 spring Bean, 进行单例模式的注入进行使用
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 * @since 2020/2/1
 * @see Cache
 * @see EhCacheTemplate
 */
@Slf4j
@Component
public class CacheTemplate {

    /**
     * 默认的缓存时间{@value}
     * <pre>
     * 根据 {@code TimeUnit} 进行判断缓存时长
     *  如果为 <code> TimeUnit.SECONDS </code>, 则缓存 2 分钟
     *  如果为 <code> TimeUnit.MINUTES </code>, 则缓存 2 小时
     * </pre>
     */
    private static final long DEFAULT_DURATION_TIME = 2 * 60L;

    /**
     * 默认的缓存空间大小{@value}
     */
    private static final int DEFAULT_INIT_CAPACITY = 10;

    /**
     * 缓存最大条数的默认值{@value}
     */
    private static final int DEFAULT_MAX_SIZE = 100;

    /**
     * Java 虚拟机 启动时初始化的 {@code Cache} 实例
     */
    private Cache<String, Object> cacheInfo;

    /**
     * 用户数手动指定 <code>Cache</code> 的参数
     */
    private Cache<String, Object> cacheInfoCustomize = Caffeine.newBuilder()
            // 设置过期时间：2分钟
            .expireAfterWrite(DEFAULT_DURATION_TIME, TimeUnit.SECONDS)
            // 初始的缓存空间大小
            .initialCapacity(DEFAULT_INIT_CAPACITY)
            // 缓存的最大条数
            .maximumSize(DEFAULT_MAX_SIZE)
            .build();

    /**
     * 验证码 key 值同步锁
     */
    private final Object synLock = new Object();

    /**
     * Associates the {@code value} with the {@code key} in this cache. If the cache previously
     * contained a value associated with the {@code key}, the old value is replaced by the new
     * {@code value}.
     * <p>
     * Prefer {@link Cache#get(Object, Function)} when using the conventional "if cached, return; otherwise
     * create, cache and return" pattern.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @throws NullPointerException if the specified key or value is null
     */
    public void setCache(@NotNull String key, @NotNull Object value) {
        cacheInfo.put(key, value);
    }

    /**
     * 设置缓存，默认的缓存空间大小 {@link #DEFAULT_INIT_CAPACITY}，缓存最大条数 {@link #DEFAULT_MAX_SIZE}
     *
     * @see #setCache(String, Object, long, TimeUnit, int, int)
     * @see #setCache(String, Object)
     */
    public void setCache(String key, Object value, long time) {
        setCache(key, value, time, null, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_SIZE);
    }

    /**
     * 设置缓存，该方法默认缓存时间按秒计时
     *
     * @see #setCache(String, Object, long, TimeUnit, int, int)
     */
    public void setCache(@NotNull String key, Object value, @NotNull long time, @NotNull int initCapacity,
                         @NotNull int maxSize) {
        setCache(key, value, time, null, initCapacity, maxSize);
    }

    /**
     * 缓存统一入口进行使用
     *
     * @see Cache#put(Object, Object)
     * @see #setCache(String, Object)
     */
    public void setCache(@NotNull String key, @NotNull Object value, @NotNull long time, TimeUnit timeUnit,
                         @NotNull int initCapacity, @NotNull int maxSize) {
        synchronized (synLock) {
            // builder模式 《Effective Java II》提倡的写法
            cacheInfoCustomize = Caffeine.newBuilder()
                    // 设置过期时间：2分钟
                    .expireAfterWrite(time, timeUnit == null ? TimeUnit.SECONDS : timeUnit)
                    // 初始的缓存空间大小
                    .initialCapacity(initCapacity)
                    // 缓存的最大条数
                    .maximumSize(maxSize)
                    .build();
            cacheInfoCustomize.put(key, value);
        }
    }

    /**
     * Returns the value associated with the {@code key} in this cache, or {@code null} if there is no
     * cached value for the {@code key}.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if this cache contains
     * no mapping for the key
     */
    public Object getCache(@NotNull String key) {
        return cacheInfo.getIfPresent(key);
    }

    /**
     * @see #getCache(String)
     */
    public Object getCacheCustomize(@NotNull String key) {
        return cacheInfoCustomize.getIfPresent(key);
    }

    /**
     * Discards any cached value for the {@code key}. The behavior of this operation is undefined for
     * an entry that is being loaded (or reloaded) and is otherwise not present.
     *
     * @param key the key whose mapping is to be removed from the cache
     */
    public void invalidate(@NotNull String key) {
        cacheInfo.invalidate(key);
    }

    /**
     * @see #invalidate(String)
     */
    public void invalidateCacheCustomize(@NotNull String key) {
        cacheInfoCustomize.invalidate(key);
    }

}
