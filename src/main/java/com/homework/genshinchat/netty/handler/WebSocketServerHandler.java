package com.homework.genshinchat.netty.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    public static final int WEBSOCKET_PORT = 8081;
    public static final String WEBSOCKET_ADDRESS = "ws://localhost:" + WEBSOCKET_PORT + "/websocket";
    private WebSocketServerHandshaker handShaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            initHttpRequest((FullHttpRequest) msg, ctx);
        } else if (msg instanceof WebSocketFrame) {
            handleWebsocketFrame((WebSocketFrame) msg, ctx);
        } else {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST
            );
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            throw new RuntimeException("俺不中嘞！！！");
        }
    }


    private static final String WEBSOCKET_SYMBOL = "websocket";
    /**
     * 初始化http请求为Websocket做准备
     *
     * @param fullHttpMessage 完整http消息
     * @param ctx             上下文
     */
    @SneakyThrows
    void initHttpRequest(FullHttpRequest fullHttpMessage, ChannelHandlerContext ctx) {
        // 校验是否可以升级
        if (!fullHttpMessage.decoderResult().isSuccess() || !WEBSOCKET_SYMBOL.equals(fullHttpMessage.headers().get("Upgrade"))) {
            log.info("无法升级");
            // 认为这个消息已经被消费了，我们没有必要继续往后面传递了
            fullHttpMessage.release();
            ctx.close();
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST
            );
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        // 升级为websocket协议
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(WEBSOCKET_ADDRESS, null, false);
        handShaker = factory.newHandshaker(fullHttpMessage);
        if (handShaker == null) {
            log.info("不能完成握手动作");
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), fullHttpMessage);
            log.info("客户端连接成功: {}", ctx.channel().remoteAddress());
        }
    }

    /**
     * websocket 消息处理方法
     *
     * @param webSocketFrame web套接字框架
     * @param ctx            上下文
     */
    void handleWebsocketFrame(WebSocketFrame webSocketFrame, ChannelHandlerContext ctx) {
        if (handShaker == null) {
            webSocketFrame.release();
            ctx.close();
            return;
        }
        // 握手关闭
        if (webSocketFrame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) webSocketFrame);
        } else if (webSocketFrame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) webSocketFrame).text();
            log.info("收到消息: {}", text);
            String serverResponse = "服务器收到消息: " + text;
            ctx.channel().writeAndFlush(new TextWebSocketFrame(serverResponse));
        } else if (webSocketFrame instanceof PingWebSocketFrame) {
            ctx.channel().writeAndFlush(new PongWebSocketFrame(webSocketFrame.content().retain()));
        }
        else if (webSocketFrame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) webSocketFrame;

        }
        else {
            ctx.close();
        }
    }
}