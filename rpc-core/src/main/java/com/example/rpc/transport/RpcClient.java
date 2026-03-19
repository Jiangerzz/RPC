package com.example.rpc.transport;

import com.example.rpc.codec.RpcMessageDecoder;
import com.example.rpc.codec.RpcMessageEncoder;
import com.example.rpc.config.RpcClientProperties;
import com.example.rpc.constant.RpcMessageType;
import com.example.rpc.exception.RpcException;
import com.example.rpc.protocol.RpcMessage;
import com.example.rpc.protocol.RpcRequest;
import com.example.rpc.protocol.RpcResponse;
import com.example.rpc.serialize.SerializerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * RPC 客户端。
 * 负责建立和复用 Netty 连接，并同步等待服务端响应。
 */
public class RpcClient {

    private final RpcClientProperties properties;
    private final EventLoopGroup group = new NioEventLoopGroup();
    private final ChannelProvider channelProvider = new ChannelProvider();
    private final UnprocessedRequests unprocessedRequests = new UnprocessedRequests();
    private final byte serializerCode;

    /**
     * 根据客户端配置创建 RPC 客户端。
     *
     * @param properties 客户端配置
     */
    public RpcClient(RpcClientProperties properties) {
        this.properties = properties;
        this.serializerCode = SerializerFactory.getSerializer(properties.getSerializer()).getCode();
    }

    /**
     * 发送一次 RPC 请求并等待响应结果。
     *
     * @param address 目标服务地址
     * @param request RPC 请求对象
     * @return RPC 响应对象
     */
    public RpcResponse sendRequest(String address, RpcRequest request) {
        try {
            Channel channel = getChannel(address);
            RpcMessage message = new RpcMessage();
            message.setMessageType(RpcMessageType.REQUEST);
            message.setCodec(serializerCode);
            message.setCompress((byte) 0);
            message.setRequestId(request.getRequestId());
            message.setData(request);
            CompletableFuture<RpcResponse> future = new CompletableFuture<RpcResponse>();
            unprocessedRequests.put(request.getRequestId(), future);
            channel.writeAndFlush(message).sync();
            return future.get(properties.getTimeoutMillis(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RpcException(RpcException.ExceptionCode.NETWORK_EXCEPTION, "Failed to send request to " + address, e);
        }
    }

    /**
     * 获取指定地址的可用连接，不存在时创建新连接。
     *
     * @param address 服务地址
     * @return 可用 Channel
     * @throws InterruptedException 连接建立过程中线程被中断时抛出
     */
    private Channel getChannel(String address) throws InterruptedException {
        Channel existing = channelProvider.get(address);
        if (existing != null) {
            return existing;
        }
        String[] parts = address.split(":");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new IdleStateHandler(0, 30, 0))
                                .addLast(new RpcMessageDecoder())
                                .addLast(new RpcMessageEncoder())
                                .addLast(new RpcClientHandler(unprocessedRequests));
                    }
                });
        Channel channel = bootstrap.connect(parts[0], Integer.parseInt(parts[1])).sync().channel();
        channelProvider.set(address, channel);
        return channel;
    }

    /**
     * 关闭客户端线程池并释放网络资源。
     */
    public void shutdown() {
        group.shutdownGracefully();
    }
}
