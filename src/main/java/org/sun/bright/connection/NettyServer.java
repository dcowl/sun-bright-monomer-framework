package org.sun.bright.connection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.sun.bright.connection.network.ConnectionChannelInboundHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class NettyServer {

    private final TcpServer tcpSever = new TcpServer();

    private boolean enabled;

    public void enable() {
        try {
            tcpSever.run();
            enabled = true;
        } catch (Exception e) {
            log.error("netty enable is error {}.", e.getMessage(), e);
        }
    }

    public void disable() throws InterruptedException {
        tcpSever.stop();
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String endpointName() {
        return NettyServer.class.getName();
    }

    private static class TcpServer {

        /**
         * 记录Netty启动监听
         */
        private final Map<Integer, ChannelFuture> channelFutures = new HashMap<>();

        /**
         * 端口集合
         */
        private final List<Integer> ports = new ArrayList<>(16);

        /**
         * 主线程 bossGroup负责接收连接，为每一个连接创建从线程，不仅接收也处理；
         */
        private final EventLoopGroup bossGroup = new NioEventLoopGroup();

        /**
         * workGroup负责接收许多客户端的读写操作，为每个请求创建处理线程，不仅接收也处理；
         */
        private final EventLoopGroup workGroup = new NioEventLoopGroup();

        /**
         *
         */
        void stop() throws InterruptedException {
            for (ChannelFuture channelFuture : channelFutures.values()) {
                channelFuture.channel().closeFuture().sync();
            }
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

        /**
         *
         */
        void run() {
            ports.add(8002);
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            ConnectionChannelInboundHandler handler = new ConnectionChannelInboundHandler();
            try {
                int mainPort = 20000;
                serverBootstrap.group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .localAddress("", mainPort)
                        .childOption(ChannelOption.SO_REUSEADDR, true)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                /* ch.pipeline().addLast(new ReadTimeoutHandler(5*60)); */
                                ch.pipeline().addLast(handler);
                            }
                        });
                for (int port : ports) {
                    ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
                    channelFutures.put(port, channelFuture);
                    channelFuture.addListener(future -> {
                        if (future.isSuccess()) {
                            log.info("Netty start success, port = {}", port);
                        } else {
                            log.info("Netty start failed, port = {}", port);
                        }
                    });
                }
            } catch (Exception e) {
                log.error("netty start error, error is {}", e.getMessage(), e);
            }
        }
    }

}
