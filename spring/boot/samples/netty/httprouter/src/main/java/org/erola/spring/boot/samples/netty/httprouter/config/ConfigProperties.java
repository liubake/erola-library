package org.erola.spring.boot.samples.netty.httprouter.config;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.erola.spring.boot.netty.httprouter.exception.ServiceUnavailableException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="org.erola.spring.boot.netty.httprouter.sample")
public class ConfigProperties {
    /**
     * http监听端口，不配置默认6666
     */
    private Integer httpPort;
    /**
     * 请求体的最大长度，不配置默认10240
     */
    private Integer maxContentLength;
    /**
     * 业务线程池相关配置
     */
    private BusinessThreadPool businessThreadPool;

    public Integer getHttpPort() {
        if(httpPort==null || httpPort<=0){
            httpPort = 6666;
        }
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }

    public Integer getMaxContentLength() {
        if(maxContentLength==null || maxContentLength<=0){
            maxContentLength = 10240;
        }
        return maxContentLength;
    }

    public void setMaxContentLength(Integer maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public BusinessThreadPool getBusinessThreadPool() {
        if(businessThreadPool == null){
            businessThreadPool = new BusinessThreadPool();
        }
        return businessThreadPool;
    }

    public void setBusinessThreadPool(BusinessThreadPool businessThreadPool) {
        this.businessThreadPool = businessThreadPool;
    }

    /**
     * 业务逻辑线程池实例
     * @return
     */
    @Bean
    public EventExecutorGroup initializeBusinessThreadPool(){
        EventExecutorGroup ret = new DefaultEventExecutorGroup(
                getBusinessThreadPool().getPoolSize(),
                null,
                getBusinessThreadPool().getMaxPendingTasks(),
                (x, y)->{
                    throw new ServiceUnavailableException();
                }
        );
        return ret;
    }

    /**
     * 业务逻辑线程池相关配置
     */
    public static class BusinessThreadPool{
        /**
         * 核心线程数，不配置默认等于CPU核心数
         */
        private Integer poolSize;
        /**
         * 最等待任务数，不配置默认Integer.MAX_VALUE
         */
        private Integer maxPendingTasks;

        public Integer getPoolSize() {
            if(poolSize==null || poolSize<0){
                poolSize = Runtime.getRuntime().availableProcessors();
            }
            return poolSize;
        }

        public void setPoolSize(Integer poolSize) {
            this.poolSize = poolSize;
        }

        public Integer getMaxPendingTasks() {
            if(maxPendingTasks==null || maxPendingTasks<0){
                maxPendingTasks = Integer.MAX_VALUE;
            }
            return maxPendingTasks;
        }

        public void setMaxPendingTasks(Integer maxPendingTasks) {
            this.maxPendingTasks = maxPendingTasks;
        }
    }
}