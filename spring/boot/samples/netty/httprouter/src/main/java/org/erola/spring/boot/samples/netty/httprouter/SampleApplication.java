package org.erola.spring.boot.samples.netty.httprouter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.erola.spring.boot.netty.httprouter.router.IRouteDispatcher;
import org.erola.spring.boot.samples.netty.httprouter.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApplication implements ApplicationRunner {
    /**
     * 项目配置实例
     */
    @Autowired
    private ConfigProperties configProperties;
    /**
     * 路由分发器实例
     */
    @Autowired
    private IRouteDispatcher routeDispatcher;
    /**
     * 执行具体业务的线程池
     */
    @Autowired
    private EventExecutorGroup businessThreadPool;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        EventLoopGroup acceptGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        serverBootstrap.group(acceptGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //.handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(configProperties.getMaxContentLength()))
                                .addLast(new HttpContentCompressor())
                                .addLast(businessThreadPool, new HttpServerHandler(routeDispatcher));
                    }
                });
        Channel channel = serverBootstrap.bind(configProperties.getHttpPort()).sync().channel();
        channel.closeFuture().sync();
    }
}