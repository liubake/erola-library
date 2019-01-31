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

import org.erola.common.util.ClassUtil;
import org.erola.common.util.StringUtil;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.locks.Lock;

/**
 *
 * @description: 本地缓存实现基类
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public abstract class BaseCache implements ILocalCache {
    /**
     * 用于执行清除超时缓存项的计时器
     */
    private Timer timer;
    /**
     * 用读写锁来同步读写操作
     */
    protected Lock readLock;
    /**
     * 用读写锁来同步读写操作
     */
    protected Lock writeLock;
    /**
     * 保存缓存项的Map，具体类型由子类初始化
     */
    protected Map<String, CacheItem> cacheMap;

    /**
     * 构造函数
     */
    public BaseCache(){
        this.timer = new Timer();
    }

    @Override
    public int size(){
        readLock.lock();
        try {
            return cacheMap.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void clear(){
        writeLock.lock();
        try {
            if(cacheMap.size()>0) {
                cacheMap.clear();
                timer.cancel();
                timer = new Timer();
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Object get(String key){
        if(key!=null && !key.isEmpty()){
            readLock.lock();
            try {
                CacheItem cacheItem = cacheMap.get(key);
                if (cacheItem != null) {
                    return cacheItem.getValue();
                }
            }finally {
                readLock.unlock();
            }
        }
        return null;
    }

    @Override
    public <T> T get(String key,  Class<T> type){
        return ClassUtil.tryCast(type, get(key));
    }

    @Override
    public <T> T get(String key, ClassUtil.TypeHolder<T> typeHolder){
        return ClassUtil.tryCast(typeHolder, get(key));
    }

    @Override
    public Object remove(String key){
        if(StringUtil.isNotNullOrEmpty(key)){
            writeLock.lock();
            try {
                CacheItem cacheItem = cacheMap.remove(key);
                if (cacheItem != null) {
                    if(cacheItem.getRemoveTimerTask() != null) {
                        cacheItem.getRemoveTimerTask().cancel();
                    }
                    if (cacheItem.getItemRemoveListener() != null) {
                        cacheItem.getItemRemoveListener().onItemRemove();
                    }
                    return cacheItem.getValue();
                }
            }finally {
                writeLock.unlock();
            }
        }
        return null;
    }

    @Override
    public <T> T remove(String key, Class<T> type) {
        return ClassUtil.tryCast(type, remove(key));
    }

    @Override
    public <T> T remove(String key, ClassUtil.TypeHolder<T> typeHolder){
        return ClassUtil.tryCast(typeHolder, remove(key));
    }

    @Override
    public boolean add(String key, Object value){
        return add(key, value, null, null);
    }

    @Override
    public boolean add(String key, Object value, long expireMilliseconds){
        return add(key, value, expireMilliseconds, null);
    }

    @Override
    public boolean add(String key, Object value, Date expireTime) {
        return add(key, value, expireTime==null ? null : expireTime.getTime()-System.currentTimeMillis(), null);
    }

    @Override
    public boolean add(String key, Object value, Long expireMilliseconds, IItemRemoveListener itemRemoveListener){
        if(StringUtil.isNotNullOrEmpty(key)){
            writeLock.lock();
            try {
                if (!cacheMap.containsKey(key)) {
                    boolean hasPeriod = expireMilliseconds!=null && expireMilliseconds>0;
                    CacheItem cacheItem = new CacheItem(this, key, value, hasPeriod, itemRemoveListener);
                    cacheMap.put(key, cacheItem);
                    if(hasPeriod) {
                        timer.schedule(cacheItem.getRemoveTimerTask(), expireMilliseconds);
                    }
                    return true;
                }
            }finally {
                writeLock.unlock();
            }
        }
        return false;
    }

    @Override
    public void set(String key, Object value){
        set(key, value, null, null);
    }

    @Override
    public void set(String key, Object value, long expireMilliseconds){
        set(key, value, expireMilliseconds, null);
    }

    @Override
    public void set(String key, Object value, Date expireTime){
        set(key, value, expireTime==null ? expireTime.getTime()-System.currentTimeMillis() : null, null);
    }

    @Override
    public void set(String key, Object value, Long expireMilliseconds, IItemRemoveListener itemRemoveListener) {
        if(StringUtil.isNotNullOrEmpty(key)) {
            writeLock.lock();
            try {
                CacheItem cacheItem = cacheMap.get(key);
                boolean hasPeriod = expireMilliseconds!=null && expireMilliseconds>0;
                if (cacheItem != null) {
                    cacheItem.updateValueTimerTaskAndListener(this, value, itemRemoveListener);
                    if(hasPeriod) {
                        timer.schedule(cacheItem.getRemoveTimerTask(), expireMilliseconds);
                    }
                } else {
                    cacheItem = new CacheItem(this, key, value, hasPeriod, itemRemoveListener);
                    cacheMap.put(key, cacheItem);
                    if(hasPeriod) {
                        timer.schedule(cacheItem.getRemoveTimerTask(), expireMilliseconds);
                    }
                }
            }finally {
                writeLock.unlock();
            }
        }
    }
}