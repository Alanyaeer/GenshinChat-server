package com.homework.chatserver.netty;

import com.homework.chatserver.netty.codec.RpcMessageDecoder;
import com.homework.chatserver.netty.codec.RpcMessageEncoder;
import com.homework.common.entity.enums.CompressTypeEnum;
import com.homework.common.entity.enums.MessageTypeEnum;
import com.homework.common.entity.enums.SerializationTypeEnum;
import com.homework.common.entity.rpc.message.TextMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;

import static com.homework.chatserver.netty.NettyChannelHandlerInitializer.MAX_HTTP_CONTENT_LENGTH;
import static com.homework.chatserver.netty.NettyChannelHandlerInitializer.MAX_WEBSOCKET_CONTENT_LENGTH;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/12
 */
@Component
@Slf4j
public class NettyWebClient {
    @SneakyThrows
    public void startConnect(){
        URI uri = URI.create("ws://localhost:8081/v2/im/server?myId=1234");

        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        NioEventLoopGroup workGroup = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new HttpClientCodec())
                                    .addLast(new ChunkedWriteHandler())
                                    .addLast(new HttpObjectAggregator(MAX_HTTP_CONTENT_LENGTH))
                                    .addLast(new WebSocketFrameAggregator(MAX_WEBSOCKET_CONTENT_LENGTH))
                                    .addLast(new WebSocketClientHandler(handshaker))
                                    .addLast(new RpcMessageDecoder())
                                    .addLast(new RpcMessageEncoder());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
            Channel channel = channelFuture.channel();

            // 等待握手完成
            WebSocketClientHandler handler = channel.pipeline().get(WebSocketClientHandler.class);
            handler.handshakeFuture().sync();

            TextMessage textMessage = TextMessage.builder()
                    .text("hello world")
                    .userId(1332L)
                    .toUserId(3224L)
                    .compressType(CompressTypeEnum.NONE.getCode())
                    .codecType(SerializationTypeEnum.PROTOSTUFF.getCode())
                    .messageType(MessageTypeEnum.TEXT.getCode())
                    .build();
            log.info(textMessage.toString());
            channel.writeAndFlush(textMessage);
            channel.closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
        }
    }
    public static class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

        private final WebSocketClientHandshaker handshaker;
        private ChannelPromise handshakeFuture;

        public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
            this.handshaker = handshaker;
        }

        public ChannelFuture handshakeFuture() {
            return handshakeFuture;
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            handshakeFuture = ctx.newPromise();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            // 连接成功时发起握手
            handshaker.handshake(ctx.channel());
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            Channel ch = ctx.channel();
            if (!handshaker.isHandshakeComplete()) {
                // 处理握手响应
                try {
                    handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                    handshakeFuture.setSuccess();
                    log.info("WebSocket 握手成功!");
                } catch (WebSocketHandshakeException e) {
                    handshakeFuture.setFailure(e);
                    log.info("WebSocket 握手失败!");
                }
                return;
            }

            // 握手完成后，处理 WebSocket 帧
            if (msg instanceof TextWebSocketFrame) {
                log.info("收到消息: " + ((TextWebSocketFrame) msg).text());
            } else if (msg instanceof PongWebSocketFrame) {
                log.info("收到 Pong");
            } else if (msg instanceof CloseWebSocketFrame) {
                log.info("连接关闭");
                ch.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            if (!handshakeFuture.isDone()) {
                handshakeFuture.setFailure(cause);
            }
            ctx.close();
        }
    }
}
