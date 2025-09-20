package com.homework.genshinchatcore.netty.handler;

import com.homework.common.entity.rpc.message.BaseMessage;
import com.homework.common.entity.rpc.message.TextMessage;
import com.homework.genshinchatcore.netty.channel.ChannelContext;
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
public class RpcMessageHandler extends SimpleChannelInboundHandler<BaseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
        if(msg instanceof TextMessage textMessage){
            Long toUserId = textMessage.getToUserId();

            // 区分发送对象
            // toUser == -1
            if(toUserId == -1L){
                Map<String, Channel> channelMap = ChannelContext.select().getChannelMap();
                log.info("channels个数: {}", channelMap.size());
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
