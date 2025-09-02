package com.homework.genshinchat.component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class NettyWebServer implements CommandLineRunner {
    public static final int PORT = 8081;
    public static final String WEBSOCKET_ADDRESS = "ws://localhost:" + PORT + "/websocket";

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

        try {
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
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            // 处理Http的协议，当websocket升级完毕之后会自动移除
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new WebSocketServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind("0.0.0.0", PORT).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
    class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>{
        private WebSocketServerHandshaker handShaker;
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.info("收到消息: {}", msg);
            if(msg instanceof FullHttpRequest){
                initHttpRequest((FullHttpRequest) msg, ctx);
            }
            else if(msg instanceof WebSocketFrame){
                handleWebsocketFrame((WebSocketFrame) msg, ctx);
            }
            else {
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST
                );
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                throw new RuntimeException("俺不中嘞！！！");
            }
        }

        /**
         * 初始化http请求为Websocket做准备
         *
         * @param fullHttpMessage 完整http消息
         * @param ctx             上下文
         */
        private static final String WEBSOCKET_SYMBOL = "websocket";
        @SneakyThrows
        void initHttpRequest(FullHttpRequest fullHttpMessage, ChannelHandlerContext ctx){
//            fullHttpMessage.decoderResult()
            // 校验是否可以升级
            if(!fullHttpMessage.decoderResult().isSuccess() || !WEBSOCKET_SYMBOL.equals(fullHttpMessage.headers().get("Upgrade"))) {
                log.info("无法升级");
                // 认为这个消息已经被消费了，我们没有必要继续往后面传递了
                fullHttpMessage.release();
                ctx.close();
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST
                );
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                return ;
            }
            // 升级为websocket协议
            WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(WEBSOCKET_ADDRESS, null, false);
            handShaker = factory.newHandshaker(fullHttpMessage);
            if(handShaker == null){
                log.info("不能完成握手动作");
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            }
            else{
                handShaker.handshake(ctx.channel(), fullHttpMessage);
                log.info("客户端连接成功: {}", ctx.channel().remoteAddress());
            }
        }

        /**
         * websocket 消息处理方法
         *
         * @param webSocketFrame web套接字框架
         * @param ctx            上下文
         */
        void handleWebsocketFrame(WebSocketFrame webSocketFrame, ChannelHandlerContext ctx){
            if(handShaker == null) {
                webSocketFrame.release();
                ctx.close();
                return;
            }
            // 握手关闭
            if(webSocketFrame instanceof CloseWebSocketFrame){
                handShaker.close(ctx.channel(), (CloseWebSocketFrame) webSocketFrame);
            }
            else if(webSocketFrame instanceof TextWebSocketFrame){
                String text = ((TextWebSocketFrame) webSocketFrame).text();
                log.info("收到消息: {}", text);
                String serverResponse = "服务器收到消息: " + text;
                ctx.channel().writeAndFlush(new TextWebSocketFrame(serverResponse));
            }
            else if(webSocketFrame instanceof PingWebSocketFrame){
                ctx.channel().writeAndFlush(new PongWebSocketFrame(webSocketFrame.content().retain()));
            }
            else{
                ctx.close();
            }
        }
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
