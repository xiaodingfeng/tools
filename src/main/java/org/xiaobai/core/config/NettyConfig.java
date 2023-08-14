package org.xiaobai.core.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xiaobai.im.handler.ServerHandler;

import javax.annotation.Resource;

@Getter
@Configuration
@EnableConfigurationProperties
public class NettyConfig {
    @Value("${netty.boss}")
    private Integer boss;
    @Value("${netty.worker}")
    private Integer worker;
    @Value("${netty.timeout}")
    private Integer timeout;
    @Value("${netty.port}")
    private Integer port;
    @Value("${netty.tcp}")
    private Integer tcp;
    @Value("${netty.authHeader}")
    private String authHeader;
    @Value("${netty.path}")
    private String path;
    //
    @Resource
    private ServerHandler serverHandler;

    /**
     * boss 线程池
     * 负责客户端连接
     *
     * @return
     */
    @Bean
    public NioEventLoopGroup boosGroup() {
        return new NioEventLoopGroup(boss);
    }

    /**
     * worker 线程池
     * 负责业务处理
     *
     * @return
     */
    @Bean
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(worker);
    }

    /**
     * 服务器启动器
     *
     * @return
     */
    @Bean
    public ServerBootstrap serverBootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup(), workerGroup())   //设置主从线程组
                .channel(NioServerSocketChannel.class) //设置nio双向通道
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout) // 指定连接超时时间
                .option(ChannelOption.SO_BACKLOG, tcp) // tcp参数 1024个队列
                .childHandler(serverHandler); // 字处理器,用于处理workerGroup中的任务
        return serverBootstrap;
    }
}

