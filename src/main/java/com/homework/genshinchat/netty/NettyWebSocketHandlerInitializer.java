package com.homework.genshinchat.netty;

import com.homework.genshinchat.netty.handler.WebSocketServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyWebSocketHandlerInitializer extends ChannelInitializer<SocketChannel> {

    public static final int MAX_HTTP_CONTENT_LENGTH = 65536;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        // 处理Http的协议，当websocket升级完毕之后会自动移除
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(MAX_HTTP_CONTENT_LENGTH));
        pipeline.addLast(new WebSocketServerHandler());
    }
}
