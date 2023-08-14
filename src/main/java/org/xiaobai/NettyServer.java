package org.xiaobai;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xiaobai.core.config.NettyConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
@Slf4j
public class NettyServer {
    @Resource
    private NettyConfig nettyConfig;
    @Resource
    private ServerBootstrap serverBootstrap;
    @Resource
    private NioEventLoopGroup boosGroup;
    @Resource
    private NioEventLoopGroup workerGroup;
    public ChannelFuture future;

    public ChannelFuture getFuture() {
        return future;
    }

    @PostConstruct
    public void start() {
        try {
            this.future = serverBootstrap.bind(nettyConfig.getPort()).sync();
            if (this.future.isSuccess()) {
                log.info("netty server server 启动完毕... port = " + nettyConfig.getPort());
            } else {
                log.error("netty server server 启动失败... port = " + nettyConfig.getPort());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
