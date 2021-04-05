package org.sun.bright.manager.cache.enums;

/**
 * 缓存所需的系统参数
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 */
public enum CacheAttribute {

    /**
     * 2分钟 = 2 * 60秒
     */
    CACHE_LOGIN_IP(2 * 60, 10, 100),
    /**
     * 5分钟
     */
    CACHE_LOGIN_NAME(30 * 60, 10, 100);

    /**
     * 初始大小
     */
    private final int initQuantity;

    /**
     * 最大数量
     */
    private final long maxQuantity;

    /**
     * 消亡时间
     */
    private final int expires;

    CacheAttribute(int initQuantity, long maxQuantity, int expires) {
        this.initQuantity = initQuantity;
        this.maxQuantity = maxQuantity;
        this.expires = expires;
    }

    public int getInitNum() {
        return initQuantity;
    }

    public long getMaxNum() {
        return maxQuantity;
    }

    public int getExpires() {
        return expires;
    }

}
