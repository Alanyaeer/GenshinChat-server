package com.homework.genshinchat.component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class NettyWebClientTest {
    public static void main(String[] args) {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class)
                    .group(eventExecutors)
                    .handler(new LoggingHandler());
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080);
            channelFuture.sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush("你好你呀呀呀");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
