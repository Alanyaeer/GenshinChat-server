package com.homework.genshinchatcore.netty.handler;

import com.homework.common.entity.bo.TextMessageBO;
import com.homework.common.utils.GsonUtils;
import com.homework.genshinchatcore.netty.channel.ChannelContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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
