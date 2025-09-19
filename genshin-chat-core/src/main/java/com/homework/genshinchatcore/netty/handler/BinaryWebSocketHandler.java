package com.homework.genshinchatcore.netty.handler;

import com.homework.common.utils.FileUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinaryWebSocketHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
        log.info("BinaryWebSocketFrame received: {}", msg);
        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeBytes(msg.content());
        log.info(byteBuf.toString());
        FileUtils.writeBinaryDataToDiskFile(byteBuf, "1.png");
        ReferenceCountUtil.release(byteBuf);
    }
}
