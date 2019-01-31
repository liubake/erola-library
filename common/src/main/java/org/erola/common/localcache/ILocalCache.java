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
import java.util.Date;

/**
 *
 * @description: 本地缓存接口
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public interface ILocalCache {
    /**
     * 获取当前缓存项的数量
     * @return 缓存项的数量
     */
    int size();

    /**
     * 清除所有缓存项
     */
    void clear();

    /**
     * 查询对应的缓存项
     * @param key 要查询的key
     * @return 对应的缓存项
     */
    Object get(String key);

    /**
     * 查询对应的缓存项，并返回指定的类型
     * @param key 要查询的key
     * @param type 类型对象
     * @param <T> 泛型参数
     * @return 对应的缓存项
     */
    <T> T get(String key,  Class<T> type);

    /**
     * 查询对应的缓存项，并返回指定的类型
     * @param key 要查询的key
     * @param typeHolder 持有目标对象类型的对象
     * @param <T> 泛型参数
     * @return 对应的缓存项
     */
    <T> T get(String key, ClassUtil.TypeHolder<T> typeHolder);

    /**
     * 删除指定的缓存项
     * @param key 要删除的key
     * @return 对应的缓存项
     */
    Object remove(String key);

    /**
     * 删除指定的缓存项，并返回指定的类型
     * @param key 要删除的key
     * @param type 类型对象
     * @param <T> 泛型参数
     * @return 对应的缓存项
     */
    <T> T remove(String key, Class<T> type);

    /**
     * 删除指定的缓存项，并返回指定的类型
     * @param key 要查询的key
     * @param typeHolder 持有目标对象类型的对象
     * @param <T> 泛型参数
     * @return 对应的缓存项
     */
    <T> T remove(String key, ClassUtil.TypeHolder<T> typeHolder);

    /**
     * 添加指定的缓存项
     * @param key 缓存项key
     * @param value 缓存项value
     * @return 添加结果：如果已有相同的缓存项，则不添加并返回false
     */
    boolean add(String key, Object value);

    /**
     * 添加指定的缓存项
     * @param key 缓存项key
     * @param value 缓存项value
     * @param expireTime 缓存项过期时间
     * @return 添加结果：如果已有相同的缓存项，则不添加并返回false
     */
    boolean add(String key, Object value, Date expireTime);

    /**
     * 添加指定的缓存项
     * @param key 缓存项key
     * @param value 缓存项value
     * @param expireMilliseconds 缓存项过期毫秒数
     * @return 添加结果：如果已有相同的缓存项，则不添加并返回false
     */
    boolean add(String key, Object value, long expireMilliseconds);

    /**
     * 添加指定的缓存项
     * @param key 缓存项key
     * @param value 缓存项value
     * @param expireMilliseconds 缓存项过期毫秒数
     * @param itemRemoveListener 缓存项过期移除回调
     * @return 添加结果：如果已有相同的缓存项，则不添加并返回false
     */
    boolean add(String key, Object value, Long expireMilliseconds, IItemRemoveListener itemRemoveListener);

    /**
     * 添加指定的缓存项，如果已存在同样的缓存项则更新，并重新开始计算过期时间
     * @param key 缓存项key
     * @param value 缓存项value
     */
    void set(String key, Object value);

    /**
     * 添加指定的缓存项，如果已存在同样的缓存项则更新，并重新开始计算过期时间
     * @param key 缓存项key
     * @param value 缓存项value
     * @param expireTime 缓存项过期时间
     */
    void set(String key, Object value, Date expireTime);

    /**
     * 添加指定的缓存项，如果已存在同样的缓存项则更新，并重新开始计算过期时间
     * @param key 缓存项key
     * @param value 缓存项value
     * @param expireMilliseconds 缓存项过期毫秒数
     */
    void set(String key, Object value, long expireMilliseconds);

    /**
     * 添加指定的缓存项，如果已存在同样的缓存项则更新，并重新开始计算过期时间
     * @param key 缓存项key
     * @param value 缓存项value
     * @param expireMilliseconds 缓存项过期毫秒数
     * @param itemRemoveListener 缓存项过期移除回调
     */
    void set(String key, Object value, Long expireMilliseconds, IItemRemoveListener itemRemoveListener);
}