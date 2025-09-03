package com.homework.genshinchat.netty;

import com.homework.genshinchat.netty.handler.BinaryWebSocketHandler;
import com.homework.genshinchat.netty.handler.TextWebSocketHandler;
import com.homework.genshinchat.netty.handler.WebSocketServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;

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
                .addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(MAX_HTTP_CONTENT_LENGTH))
                .addLast(new WebSocketFrameAggregator(MAX_WEBSOCKET_CONTENT_LENGTH))
                // 压缩，暂时不需要
//                .addLast(new WebSocketServerCompressionHandler())
                .addLast(new WebSocketServerProtocolHandler("/v2/im/server", null, true, MAX_WEBSOCKET_CONTENT_LENGTH))
                .addLast(eventExecutors, new TextWebSocketHandler())
                .addLast(eventExecutors, new BinaryWebSocketHandler())
                ;
    }
}
