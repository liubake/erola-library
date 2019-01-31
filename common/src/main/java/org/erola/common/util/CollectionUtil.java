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
package org.erola.common.util;

import java.util.Collection;

/**
 *
 * @description: 集合操作工具类
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public class CollectionUtil {
    /**
     * 判断集合是否为null或空
     * @param value 集合对象
     * @param <T> 泛型参数
     * @return 是否为null或空
     */
    public static <T extends Collection> boolean isNullOrEmpty(T value){
        return value==null || value.isEmpty();
    }

    /**
     * 判断集合是否不为null且不为空
     * @param value 集合对象
     * @param <T> 泛型参数
     * @return 是否不为null且不为空
     */
    public static <T extends Collection> boolean isNotNullOrEmpty(T value){
        return !isNullOrEmpty(value);
    }
}