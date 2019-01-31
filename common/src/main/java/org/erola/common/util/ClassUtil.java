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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 * @description: 对象类型操作工具类
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public class ClassUtil {
    /**
     * 把对象转换为指定的类型
     * @param targetTpye 目标对象类型
     * @param value 对象对应的值
     * @param <T> 泛型参数
     * @return 如果对象为null或者转换失败则返回null
     */
    public static <T> T tryCast(Class<T> targetTpye, Object value){
        if(value != null){
            try {
                return targetTpye.cast(value);
            }catch (ClassCastException ex){
                //屏蔽转换异常
            }
        }
        return null;
    }

    /**
     * 把对象转换为指定的复杂泛型
     * @param typeHolder 泛型类型持有对象
     * @param value 对象对应的值
     * @param <T> 泛型参数
     * @return 如果对象为null或者转换失败则返回null
     */
    public static <T> T tryCast(TypeHolder <T> typeHolder, Object value){
        if(value != null){
            try {
                return (T)value;
            }catch (ClassCastException ex){
                //屏蔽转换异常
            }
        }
        return null;
    }

    /**
     * 持有泛型类型的对象
     */
    public abstract class TypeHolder <T> implements Comparable<TypeHolder<T>> {
        protected final Type _type;

        protected TypeHolder() {
            Type superClass = getClass().getGenericSuperclass();
            if (superClass instanceof Class<?>) {
                // sanity check, should never happen
                throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
            }
            _type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }

        public Type getType() {
            return _type;
        }

        @Override
        public int compareTo(TypeHolder<T> holder) {
            return 0;
        }
    }
}