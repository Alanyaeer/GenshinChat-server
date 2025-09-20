package com.homework.genshinchatcore.netty.handler.client;

import com.homework.common.entity.rpc.message.BaseMessage;
import com.homework.common.entity.rpc.message.TextMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcMessageInboundHandler extends SimpleChannelInboundHandler<BaseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
        if(msg instanceof TextMessage textMessage){
            log.info("接收到来自{}的消息，消息内容为{}", textMessage.getUserId(), textMessage.getText());
        }
    }
}
