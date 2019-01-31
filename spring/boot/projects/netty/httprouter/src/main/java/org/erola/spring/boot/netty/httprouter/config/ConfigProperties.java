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
package org.erola.spring.boot.netty.httprouter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @description: 项目配置类
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
@ConfigurationProperties(prefix = "org.erola.spring.boot.netty.httprouter")
public class ConfigProperties {
    /**
     * 是否区分大小写
     */
    private Boolean caseSensitive;

    /**
     * 是否使用路径缓存
     */
    private Boolean usePathCache;

    /**
     * 路径缓存的最大记录数
     * 超出设置的数量则按照LRU规则进行淘汰
     */
    private Integer pathCacheMaxCount;


    public Boolean getCaseSensitive() {
        return caseSensitive == null ? false : caseSensitive;
    }

    public void setCaseSensitive(Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public Boolean getUsePathCache() {
        return usePathCache == null ? true : caseSensitive;
    }

    public void setUsePathCache(Boolean usePathCache) {
        this.usePathCache = usePathCache;
    }

    public Integer getPathCacheMaxCount() {
        return (pathCacheMaxCount == null || pathCacheMaxCount<=0) ? 1024 : pathCacheMaxCount;
    }

    public void setPathCacheMaxCount(Integer pathCacheMaxCount) {
        this.pathCacheMaxCount = pathCacheMaxCount;
    }
}