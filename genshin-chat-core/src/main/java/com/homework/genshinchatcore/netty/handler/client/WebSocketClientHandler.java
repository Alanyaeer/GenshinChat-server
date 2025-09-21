package com.homework.genshinchatcore.netty.handler.client;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

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
            else if(msg instanceof BinaryWebSocketFrame binaryWebSocketFrame){
                ctx.fireChannelRead(binaryWebSocketFrame.retain());
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