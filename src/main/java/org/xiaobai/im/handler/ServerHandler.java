package org.xiaobai.im.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ServerHandler extends ChannelInitializer<SocketChannel> {
    @Value("${netty.authHeader}")
    private String authHeader;
    @Value("${netty.path}")
    private String path;

    /**
     * 初始化通道以及配置对应管道的处理器
     *
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        log.info("有新的连接");
        //获取工人所要做的工程(管道器==管道器对应的便是管道channel)
        ChannelPipeline pipeline = ch.pipeline();
        //为工人的工程按顺序添加工序/材料 (为管道器设置对应的handler也就是控制器)
        //1.设置心跳机制
        pipeline.addLast("idle-state", new IdleStateHandler(
                0,
                0,
                30,
                TimeUnit.SECONDS));
        //2.出入站时的控制器，大部分用于针对心跳机制

        //3.加解码
        pipeline.addLast("http-codec", new HttpServerCodec());
        //3.打印控制器，为工人提供明显可见的操作结果的样式
        //pipeline.addLast("logging", new LoggingHandler(LogLevel.INFO));
        pipeline.addLast("aggregator", new HttpObjectAggregator(8192));
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        // 将自己的授权handler替换原有的handler
        pipeline.addLast("auth", new SecurityServerHandler(
                path,
                "websocket",
                true,
                655360,
                false,
                authHeader,
                true
        ));
        //7.自定义的handler针对业务
        pipeline.addLast("chat-handler", new WebSocketHandler());
    }
}
