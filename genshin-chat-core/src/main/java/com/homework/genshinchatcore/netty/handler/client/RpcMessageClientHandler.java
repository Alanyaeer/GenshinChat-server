package com.homework.genshinchatcore.netty.handler.client;

import com.homework.common.entity.rpc.message.BaseMessage;
import com.homework.common.entity.rpc.message.DeliverAckMessage;
import com.homework.common.entity.rpc.message.ReceiveAckMessage;
import com.homework.common.entity.rpc.message.TextMessage;
import com.homework.common.utils.timewheel.TimeWheelManager;
import com.homework.genshinchatcore.context.SpringContextHolder;
import com.homework.genshinchatcore.netty.handler.ack.AckMessageManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class RpcMessageClientHandler extends SimpleChannelInboundHandler<BaseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
        if(msg instanceof TextMessage textMessage){
            log.info("receive message which id: {} and content: {}", textMessage.getUserId(), textMessage.getText());
        }
        else if(msg instanceof ReceiveAckMessage receiveAckMessage){
            AckMessageManager ackMessageManager = SpringContextHolder.getBean(AckMessageManager.class);
            ackMessageManager.receiveAckMessage(receiveAckMessage.getId());
            log.info("[receive ack {}]", receiveAckMessage.getId());
        }
        else if(msg instanceof DeliverAckMessage deliverAckMessage){
            log.info("[deliver ack {}]", deliverAckMessage.getId());
        }
    }
}
