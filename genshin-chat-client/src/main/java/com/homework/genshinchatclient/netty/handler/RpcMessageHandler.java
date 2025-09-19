package com.homework.genshinchatclient.netty.handler;

import com.homework.common.entity.rpc.message.BaseMessage;
import com.homework.common.entity.rpc.message.TextMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/13
 */
public class RpcMessageHandler extends SimpleChannelInboundHandler<BaseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
        if(msg instanceof TextMessage textMessage){
            System.out.println("收到消息：" + textMessage.getText());
        }
    }
}
