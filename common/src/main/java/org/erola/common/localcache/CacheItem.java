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

import java.util.TimerTask;

/**
 *
 * @description: 本地缓存项对象定义
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public class CacheItem {
    /**
     * 缓存项key
     */
    private String key;
    /**
     * 缓存项value
     */
    private Object value;
    /**
     * 执行清除缓存项的计时器任务
     */
    private RemoveTimerTask removeTimerTask;
    /**
     * 缓存项清除的监听器
     */
    private IItemRemoveListener itemRemoveListener;

    /**
     * 获取缓存项key
     * @return 缓存项key
     */
    public String getKey() {
        return key;
    }

    /**
     * 获取缓存项value
     * @return 缓存项value
     */
    public Object getValue() {
        return value;
    }

    /**
     * 获取清除缓存项的计时器任务
     * @return 清除缓存项的计时器任务
     */
    public RemoveTimerTask getRemoveTimerTask() {
        return removeTimerTask;
    }

    /**
     * 获取缓存项清除的监听器
     * @return 缓存项清除的监听器
     */
    public IItemRemoveListener getItemRemoveListener() {
        return itemRemoveListener;
    }

    /**
     * 创建没有清除监听器的缓存项
     * @param baseCache 对应的缓存实例
     * @param key 缓存项key
     * @param value 缓存项value
     */
    public CacheItem(BaseCache baseCache, String key, Object value){
        this(baseCache, key, value, false, null);
    }

    /**
     * 创建带有清除监听器的缓存项
     * @param baseCache 对应的缓存实现类
     * @param key 缓存项key
     * @param value 缓存项value
     * @param hasPeriod 是否有有效期
     * @param itemRemoveListener 缓存项清除监听器
     */
    public CacheItem(BaseCache baseCache, String key, Object value, boolean hasPeriod, IItemRemoveListener itemRemoveListener){
        this.key = key;
        this.value = value;
        this.itemRemoveListener = itemRemoveListener;
        this.removeTimerTask = hasPeriod ? new RemoveTimerTask(baseCache, key) : null;
    }

    /**
     * 更新缓存项值取消老的缓存项监听器，并创建新的缓存项监听器
     * @param baseCache 对应的缓存实现类
     * @param value 缓存项value
     * @param itemRemoveListener 缓存项清除监听器
     */
    public void updateValueTimerTaskAndListener(BaseCache baseCache, Object value, IItemRemoveListener itemRemoveListener){
        if(this.removeTimerTask != null)
            this.removeTimerTask.cancel();
        this.value = value;
        this.itemRemoveListener = itemRemoveListener;
        this.removeTimerTask = new RemoveTimerTask(baseCache, key);
    }

    /**
     * 自定义的清除缓存项的TimerTask
     */
    public class RemoveTimerTask extends TimerTask {
        /**
         * 缓存项key
         */
        private String key;
        /**
         * 对应的缓存实例
         */
        private BaseCache baseCache;

        /**
         * 构造函数
         * @param baseCache 对应的缓存实例
         * @param key 对应的缓存项key
         */
        public RemoveTimerTask(BaseCache baseCache, String key){
            this.key = key;
            this.baseCache = baseCache;
        }

        /**
         * 执行缓存项清除的操作
         */
        @Override
        public void run() {
            if(baseCache != null){
                baseCache.remove(key);
            }
        }
    }
}