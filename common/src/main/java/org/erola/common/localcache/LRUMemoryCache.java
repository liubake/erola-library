/*
 *                               .__
 *      ____   _______    ____   |  |   _____
 *    _/ __ \  \_  __ \  /  _ \  |  |   \__  \
 *    \  ___/   |  | \/ (  <_> ) |  |__  / __ \_
 *     \___  >  |__|     \____/  |____/ (____  /
 *         \/                                \/
 *
 *    - - - - - bake.liu@outlook.com - - - - -
 *
 */
package org.erola.common.localcache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @description: LRU 本地缓存实现
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public class LRUMemoryCache extends BaseCache {
    /**
     * LRUMemoryCache 构造函数
     * @param maxItemCount
     */
    public LRUMemoryCache(Integer maxItemCount){
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
        this.cacheMap = new LRUConcurrentLinkedHashMap<>(maxItemCount);
    }

    /**
     * 基于读写锁和LinkedHashMap实现的线程安全的LRU HashMap
     * @param <K> 泛型参数
     * @param <V> 泛型参数
     */
    private class LRUConcurrentLinkedHashMap<K, V extends CacheItem> extends LinkedHashMap<K, V> {
        /**
         * 默认缓存项的最大容量
         */
        private int maxItemCount = 1<<16;
        /**
         * 增长因子
         */
        private static final float DEFAULT_LOAD_FACTOR = 0.75f;
        /**
         * 初始容量
         */
        private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

        /**
         * 构造函数
         */
        public LRUConcurrentLinkedHashMap(Integer maxItemCount){
            super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true);
            if(maxItemCount!=null && maxItemCount>0){
                this.maxItemCount = maxItemCount;
            }
        }

        /**
         * 重写removeEldestEntry
         * 当缓存数量大于maxItemCount时移除最长时间没被访问过的项,同时调用对应的移除监听器
         * </span>
         * @param eldest 最长时间没有访问的缓存项
         * @return 是否需要移除
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            /**
             * 不要 return true，这样会交给LinkedHashMap移除对应项（LinkedHashMap 内部没有线程安全保障）
             * 交由 LRUMemoryCache 的 remove 执行后，直接返回false
             */
            boolean needRemoveEldest = LRUMemoryCache.this.size() > maxItemCount;
            if(needRemoveEldest && eldest!=null && eldest.getValue()!=null){
                LRUMemoryCache.this.remove(eldest.getValue().getKey());
            }
            return false;
        }
    }
}