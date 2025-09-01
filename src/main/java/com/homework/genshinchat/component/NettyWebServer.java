package com.homework.genshinchat.component;

import com.homework.genshinchat.codec.MessageDecoder;
import com.homework.genshinchat.codec.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Component
public class NettyWebServer implements CommandLineRunner {
    public static final int PORT = 5377;

    // 创建一个线程池，用于执行Netty服务器的启动任务
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> new Thread(r, "netty-web-server"));
    @Override
    public void run(String... args) throws Exception {
        executorService.submit(this::autoRegister);
    }
    @SneakyThrows
    private void autoRegister(){

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                // option 是配置父Channel的（监听端口通道NioServerSocketChannel）
                .option(ChannelOption.SO_BACKLOG, 128)
                // childOption 是配置子Channel的（客户端连接通道NioSocketChannel）
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 30, 0, TimeUnit.SECONDS));
                        pipeline.addLast(new MessageDecoder());
                        pipeline.addLast(new MessageEncoder());
                        pipeline.addLast(new NettyWebServerHandler());
                    }
                });
        String localhost = InetAddress.getLocalHost().getHostAddress();
        ChannelFuture channelFuture = serverBootstrap.bind(localhost, PORT).sync();
        channelFuture.channel().closeFuture().sync();
    }
    static class NettyWebServerHandler extends ChannelDuplexHandler {
        public NettyWebServerHandler(){}

        /**
         * 用户事件已触发
         *
         * @param ctx 上下文
         * @param evt 向
         * @throws Exception 例外
         */
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if(evt instanceof IdleState){
                IdleState state = (IdleState) evt;
                if(state == IdleState.WRITER_IDLE){

                }
            }
            else {
                ctx.fireUserEventTriggered(evt);
            }
        }

        /**
         * 入站读取数据
         *
         * @param ctx 上下文
         * @param msg 消息
         * @throws Exception 例外
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ctx.fireChannelRead(msg);
        }
    }
}
