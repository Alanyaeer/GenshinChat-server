package com.homework.genshinchat.netty.handler;

import com.homework.genshinchat.entity.bo.TextMessageBO;
import com.homework.genshinchat.netty.channel.ChannelContext;
import com.homework.genshinchat.netty.channel.SingletonChannelManager;
import com.homework.genshinchat.utils.GsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String message = msg.text();
        TextMessageBO textMessageBO = GsonUtils.fromJson(message, TextMessageBO.class);
        Channel channel = ChannelContext.select().getChannel(textMessageBO.getTo());
        channel.writeAndFlush(new TextWebSocketFrame(textMessageBO.getText()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.error("服务器发送异常{}", cause.toString());
    }
}
