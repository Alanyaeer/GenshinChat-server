package com.homework.chatserver.netty;

import com.homework.genshinchatcore.netty.NettyChannelHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class NettyWebServer implements CommandLineRunner {

    public static final int PORT = 8081;

    // 创建一个线程池，用于执行Netty服务器的启动任务
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> new Thread(r, "netty-web-server"));
    @Override
    public void run(String... args) throws Exception {
        executorService.submit(this::autoRegister);
    }
    @SneakyThrows
    private void autoRegister(){

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        DefaultEventExecutorGroup eventExecutors = new DefaultEventExecutorGroup(
                Runtime.getRuntime().availableProcessors() * 2,
                new DefaultThreadFactory("netty-handle-event-thread", false)
        );
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    // option 是配置父Channel的（监听端口通道NioServerSocketChannel）
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // childOption 是配置子Channel的（客户端连接通道NioSocketChannel）
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new NettyChannelHandlerInitializer(eventExecutors));
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
//            nettyWebClient.startConnect("333");
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            eventExecutors.shutdownGracefully();
        }
    }
}
