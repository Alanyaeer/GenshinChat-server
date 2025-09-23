package com.homework.genshinchatclient.netty;


import com.homework.common.entity.enums.CompressTypeEnum;
import com.homework.common.entity.enums.MessageTypeEnum;
import com.homework.common.entity.enums.SerializationTypeEnum;
import com.homework.common.entity.rpc.message.TextMessage;
import com.homework.common.utils.timewheel.TimeWheelManager;
import com.homework.genshinchatcore.netty.codec.RpcMessageDecoder;
import com.homework.genshinchatcore.netty.codec.RpcMessageEncoder;
import com.homework.genshinchatcore.context.SpringContextHolder;
import com.homework.genshinchatcore.idGenerator.IdGenerator;
import com.homework.genshinchatcore.netty.handler.ack.AckMessageManager;
import com.homework.genshinchatcore.netty.handler.client.RpcMessageClientHandler;
import com.homework.genshinchatcore.netty.handler.client.WebSocketBinaryFrameToByteBufHandler;
import com.homework.genshinchatcore.netty.handler.client.WebSocketClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.concurrent.DefaultThreadFactory;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.homework.genshinchatcore.netty.NettyChannelHandlerInitializer.MAX_HTTP_CONTENT_LENGTH;
import static com.homework.genshinchatcore.netty.NettyChannelHandlerInitializer.MAX_WEBSOCKET_CONTENT_LENGTH;


/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/12
 */
@Component
@Slf4j
public class NettyWebClient implements CommandLineRunner {


    private static final HashedWheelTimer HASHED_WHEEL_TIMER = new HashedWheelTimer(
            r -> new Thread(r, "netty-web-client-resend-msg-thread"),
            100,
            TimeUnit.MILLISECONDS,
            512,
            true);
    @SneakyThrows
    public void startConnect(){
        IdGenerator snowFlakeIdGenerator = SpringContextHolder.getBean("snowFlakeIdGenerator", IdGenerator.class);
        long userId = snowFlakeIdGenerator.nextId();
        log.info("当前用户的id为 {}", userId);
        URI uri = URI.create("ws://localhost:8081/v2/im/server?myId=" + userId);
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        NioEventLoopGroup workGroup = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new HttpClientCodec())
                                    .addLast(new ChunkedWriteHandler())
                                    .addLast(new HttpObjectAggregator(MAX_HTTP_CONTENT_LENGTH))
                                    .addLast(new WebSocketFrameAggregator(MAX_WEBSOCKET_CONTENT_LENGTH))
                                    .addLast(new WebSocketClientHandler(handshaker))
//                                    .addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast(new WebSocketBinaryFrameToByteBufHandler())
                                    .addLast(new RpcMessageDecoder())
                                    .addLast(new RpcMessageEncoder())
                                    .addLast(new RpcMessageClientHandler())
                            ;
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
            Channel channel = channelFuture.channel();

            // 等待握手完成
            WebSocketClientHandler handler = channel.pipeline().get(WebSocketClientHandler.class);
            handler.handshakeFuture().sync();
            int messageContent = 0;

            // 每隔一段时间发送一条消息
            TimeWheelManager timeWheelManager = TimeWheelManager.getInstance();
            AckMessageManager ackMessageManager = SpringContextHolder.getBean(AckMessageManager.class);
            while(true){
                TextMessage textMessage = TextMessage.builder()
                        .text(String.valueOf(messageContent))
                        .userId(userId)
                        .toUserId(-1L)
                        .compressType(CompressTypeEnum.NONE.getCode())
                        .codecType(SerializationTypeEnum.KRYO.getCode())
                        .messageType(MessageTypeEnum.TEXT.getCode())
                        .id(snowFlakeIdGenerator.nextId())
                        .build();
                channel.writeAndFlush(textMessage);
                log.info("发送消息成功, 消息内容为：{}", textMessage.getId());
                timeWheelManager.addTask(new TimeWheelManager.TaskExecutor() {
                    @Override
                    public boolean shouldExecuteTask() {
                        return !ackMessageManager.containAckMessage(textMessage.getId());
                    }

                    @Override
                    public boolean shouldScheduleNext() {
                        return shouldExecuteTask();
                    }

                    @Override
                    public void doExecuteTask() {
                        channel.writeAndFlush(textMessage);
                        log.info("没有收到ACK，重新发送消息成功, 消息id：{}", textMessage.getId());
                    }
                }, 5, TimeUnit.SECONDS);

                // 实际上 5s 10s 20s 40 超过一分钟没有接收到判定本次消息没有发送成功
                messageContent++;
                Thread.sleep(5000);
            }
        } finally {
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(()-> {
            startConnect();
        }).start();
    }
}
