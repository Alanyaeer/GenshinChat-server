package com.homework.chatserver.netty.handler;

import com.homework.chatserver.netty.channel.ChannelContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.homework.common.entity.constants.MessageMetaConstants.MY_ID;

@Slf4j
public class WebSocketConnectionMetaHandler extends ChannelInboundHandlerAdapter{



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 握手成功事件
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete complete =
                    (WebSocketServerProtocolHandler.HandshakeComplete) evt;

            String uri = complete.requestUri(); // e.g. /v2/im/server?userId=123&token=abc
            QueryStringDecoder decoder = new QueryStringDecoder(uri);
            Map<String, List<String>> params = decoder.parameters();

            String userId = null;
            if (params.containsKey(MY_ID)) {
                userId = params.get(MY_ID).get(0);
            }

            if (userId != null) {
                Channel channel = ctx.channel();
                channel.attr(AttributeKey.valueOf(MY_ID)).set(userId);
                ChannelContext.select().registerChannel(userId, channel);
                System.out.println("用户 " + userId + " 绑定成功");
            } else {
                System.out.println("握手缺少 userId 参数");
                ctx.close();
            }
        }
        else if(evt instanceof IdleStateEvent idleStateEvent){
            if(IdleState.READER_IDLE.equals(idleStateEvent.state())){
                log.info("readable timeout close the connection");
                ctx.close();
            }
        }
        else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 客户端断开连接时清理
        AttributeKey<String> key = AttributeKey.valueOf(MY_ID);
        String userId = ctx.channel().attr(key).get();
        if (userId != null) {
            ChannelContext.select().unRegisterChannel(userId);
            System.out.println("❌ 用户 " + userId + " 断开连接");
        }
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("异常: " + cause.getMessage());
        ctx.close();
    }
}
