package com.homework.chatserver.client;

import com.homework.common.entity.rpc.message.TextMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/12
 */
@Slf4j
public class NettyWebSocketClient {
    @SneakyThrows
    public static void main(String[] args) {
//        NioEventLoopGroup workGroup = new NioEventLoopGroup(1);
        try {
//            Bootstrap bootstrap = new Bootstrap()
//                    .group(workGroup)
//                    .channel(NioSocketChannel.class)
//                    .handler(new ChannelInitializer<Channel>() {
//                        @Override
//                        protected void initChannel(Channel channel) throws Exception {
//
//                        }
//                    });
//            ChannelFuture channelFuture = bootstrap.connect("localhost", 8082).sync();
//            Channel channel = channelFuture.channel();
            TextMessage textMessage = TextMessage.builder()
                    .text("hello world")
                    .userId(1332L)
                    .toUserId(3224L)
                    .build();
            log.info(textMessage.toString());
//            channel.writeAndFlush(textMessage);
//            channel.writeAndFlush()
//            channel.closeFuture().sync();
        } finally {
//            workGroup.shutdownGracefully();
        }
    }
}
