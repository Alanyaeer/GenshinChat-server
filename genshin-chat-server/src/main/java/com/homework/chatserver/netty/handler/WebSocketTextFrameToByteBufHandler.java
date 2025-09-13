package com.homework.chatserver.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class WebSocketTextFrameToByteBufHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        String text = frame.text();
        // 如果内容是 Base64，就先解码
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = ctx.alloc().buffer(bytes.length).writeBytes(bytes);
        ctx.fireChannelRead(buf);
    }
}
