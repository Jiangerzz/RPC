package com.example.rpc.server;

import com.example.rpc.codec.RpcMessageDecoder;
import com.example.rpc.codec.RpcMessageEncoder;
import com.example.rpc.config.RpcServerProperties;
import com.example.rpc.serialize.SerializerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * RPC 服务端启动器。
 * 负责初始化 Netty 服务端、安装处理链并暴露本地服务。
 */
public class RpcServer {

    private final RpcServerProperties properties;
    private final ServiceProvider serviceProvider = new ServiceProvider();
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    /**
     * 使用服务端配置创建启动器。
     *
     * @param properties 服务端配置
     */
    public RpcServer(RpcServerProperties properties) {
        this.properties = properties;
    }

    /**
     * 向本地服务容器添加一个可暴露的服务实现。
     *
     * @param serviceInterface 服务接口
     * @param serviceImpl 服务实现
     * @param <T> 服务泛型
     */
    public <T> void addService(Class<T> serviceInterface, T serviceImpl) {
        serviceProvider.addService(serviceInterface.getName(), serviceImpl);
    }

    /**
     * 启动 Netty 服务端并阻塞等待关闭。
     *
     * @throws InterruptedException 启动过程中线程被中断时抛出
     */
    public void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        final byte serializerCode = SerializerFactory.getSerializer(properties.getSerializer()).getCode();
        final RpcRequestHandler requestHandler = new RpcRequestHandler(serviceProvider);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new IdleStateHandler(60, 0, 0))
                                .addLast(new RpcMessageDecoder())
                                .addLast(new RpcMessageEncoder())
                                .addLast(new RpcServerHandler(requestHandler, serializerCode));
                    }
                });
        ChannelFuture future = bootstrap.bind(properties.getHost(), properties.getPort()).sync();
        future.channel().closeFuture().sync();
    }

    /**
     * 关闭 Netty 线程池并释放服务端资源。
     */
    public void stop() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }
}
