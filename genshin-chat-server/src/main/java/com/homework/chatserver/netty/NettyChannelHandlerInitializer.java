package com.homework.chatserver.netty;

import com.homework.chatserver.netty.handler.BinaryWebSocketHandler;
import com.homework.chatserver.netty.handler.TextWebSocketHandler;
import com.homework.chatserver.netty.handler.WebSocketConnectionMetaHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

public class NettyChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {
    private final EventExecutorGroup eventExecutors;

    public NettyChannelHandlerInitializer(EventExecutorGroup eventExecutors){
        this.eventExecutors = eventExecutors;
    }
    public static final int MAX_HTTP_CONTENT_LENGTH = 65536;

    public static final int MAX_WEBSOCKET_CONTENT_LENGTH = 10 * 1024 * 1024;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        socketChannel.pipeline()
                .addLast(new LoggingHandler(LogLevel.INFO))
                .addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS))
                .addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(MAX_HTTP_CONTENT_LENGTH))
                .addLast(new WebSocketFrameAggregator(MAX_WEBSOCKET_CONTENT_LENGTH))
                // 压缩，暂时不需要, 后面看一下怎么用
//                .addLast(new WebSocketServerCompressionHandler(0))
                .addLast(new WebSocketServerProtocolHandler("/v2/im/server", null, true, MAX_WEBSOCKET_CONTENT_LENGTH, false, true))
                .addLast(new WebSocketConnectionMetaHandler())
                .addLast(eventExecutors, new TextWebSocketHandler())
                .addLast(eventExecutors, new BinaryWebSocketHandler())
                ;
    }
}
