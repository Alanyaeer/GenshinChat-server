package com.homework.chatserver.netty.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class WebSocketBinaryFrameToByteBufHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame webSocketFrame) throws Exception {
        ctx.fireChannelRead(webSocketFrame.content());

        //        String text = frame.text();
//        // 如果内容是 Base64，就先解码
//        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
//        ByteBuf buf = ctx.alloc().buffer(bytes.length).writeBytes(bytes);
//        ctx.fireChannelRead(buf);
    }
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {

//    }
}