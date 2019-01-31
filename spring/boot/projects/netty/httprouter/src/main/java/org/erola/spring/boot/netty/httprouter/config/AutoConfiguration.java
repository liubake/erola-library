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

import org.erola.spring.boot.netty.httprouter.router.IRouteDispatcher;
import org.erola.spring.boot.netty.httprouter.router.RouteDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @description: spring boot自动装配配置
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
@Configuration
@EnableConfigurationProperties(org.erola.spring.boot.netty.httprouter.config.ConfigProperties.class)
public class AutoConfiguration {
    /**
     * 注入配置属性
     */
    @Autowired
    private ConfigProperties configProperties;

    /**
     *
     * @return
     */
    @Bean(name = "routeDispatcher")
    @ConditionalOnMissingBean(IRouteDispatcher.class)
    public IRouteDispatcher routeDispatcher(){
        IRouteDispatcher routeDispatcher = new RouteDispatcher(configProperties.getCaseSensitive(),
                configProperties.getUsePathCache(), configProperties.getPathCacheMaxCount());
        return routeDispatcher;
    }
}