package com.homework.genshinchatcore.netty.handler;

import com.homework.common.entity.constants.RpcConstants;
import com.homework.common.entity.rpc.message.BaseMessage;
import com.homework.common.entity.rpc.message.ReceiveAckMessage;
import com.homework.common.entity.rpc.message.TextMessage;
import com.homework.genshinchatcore.context.SpringContextHolder;
import com.homework.genshinchatcore.idempotent.Idempotent;
import com.homework.genshinchatcore.netty.channel.ChannelContext;
import com.homework.genshinchatcore.store.MessageStore;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/13
 */
@Slf4j
public class RpcMessageServerHandler extends SimpleChannelInboundHandler<BaseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
        // 幂等判断
        Idempotent idempotent = SpringContextHolder.getBean(RpcConstants.IDEMPOTENT_VERSION.getName(),Idempotent.class);
        boolean acquireIdempotentLock = idempotent.acquireIdempotentLock(msg.getId());
        if(!acquireIdempotentLock){
            log.info("接收到消息id{}重复，丢弃消息", msg.getId());
        }
        if(msg instanceof TextMessage textMessage){
            Long toUserId = textMessage.getToUserId();
            // 消息存储
            SpringContextHolder.getBean(RpcConstants.MESSAGE_STORE_VERSION.getName(), MessageStore.class).storeMessage(msg);

            // 发送ack消息告知用户，服务端已经保存消息
            ReceiveAckMessage receiveAckMessage = new ReceiveAckMessage();
            receiveAckMessage.fillHeaderFields(textMessage);
            ChannelContext.select().getChannel(textMessage.getUserId().toString()).writeAndFlush(receiveAckMessage);


            // 区分发送对象
            // toUser == -1
            if(toUserId == -1L){
                Map<String, Channel> channelMap = ChannelContext.select().getChannelMap();
                channelMap.forEach((k,v)->{
                    if(!k.equals(String.valueOf(textMessage.getUserId()))){
                        channelMap.get(k).writeAndFlush(textMessage);
                    }
                });
            }
            else {
                Channel channel = ChannelContext.select().getChannel(toUserId.toString());
                if(channel == null || !channel.isActive()){
                    log.info("用户 {}不在线", toUserId);
                }else{
                    channel.writeAndFlush(msg);
                }
            }
        }
    }
}
