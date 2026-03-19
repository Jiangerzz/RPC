package com.example.rpc.transport;

import com.example.rpc.constant.RpcConstants;
import com.example.rpc.constant.RpcMessageType;
import com.example.rpc.protocol.RpcMessage;
import com.example.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Netty 客户端处理器。
 * 负责接收服务端响应、处理心跳回包以及异常关闭。
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private final UnprocessedRequests unprocessedRequests;

    /**
     * 创建客户端处理器。
     *
     * @param unprocessedRequests 未完成请求管理器
     */
    public RpcClientHandler(UnprocessedRequests unprocessedRequests) {
        this.unprocessedRequests = unprocessedRequests;
    }

    /**
     * 处理客户端收到的消息。
     *
     * @param ctx Netty 上下文
     * @param msg RPC 消息
     */
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) {
        if (msg.getMessageType() == RpcMessageType.HEARTBEAT_RESPONSE) {
            return;
        }
        unprocessedRequests.complete((RpcResponse) msg.getData());
    }

    /**
     * 在空闲事件触发时主动发送心跳包。
     *
     * @param ctx Netty 上下文
     * @param evt Netty 事件对象
     */
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        RpcMessage heartbeat = new RpcMessage();
        heartbeat.setMessageType(RpcMessageType.HEARTBEAT_REQUEST);
        heartbeat.setCodec((byte) 1);
        heartbeat.setCompress((byte) 0);
        heartbeat.setRequestId(String.valueOf(System.nanoTime() & Integer.MAX_VALUE));
        heartbeat.setData(RpcConstants.HEARTBEAT_PING);
        ctx.writeAndFlush(heartbeat);
    }

    /**
     * 处理客户端异常并关闭连接。
     *
     * @param ctx Netty 上下文
     * @param cause 异常对象
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
