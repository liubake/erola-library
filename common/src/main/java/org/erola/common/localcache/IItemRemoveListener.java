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

/**
 *
 * @description: 缓存移除监听器接口定义
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public interface IItemRemoveListener {
    /**
     * 在缓存项移除时触发的操作
     */
    void onItemRemove();
}