package com.homework.genshinchatclient.netty.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class WebSocketBinaryFrameToByteBufHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame webSocketFrame) throws Exception {
        ctx.fireChannelRead(webSocketFrame.content());
    }
}