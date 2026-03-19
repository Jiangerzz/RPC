package com.example.rpc.server;

import com.example.rpc.constant.RpcConstants;
import com.example.rpc.constant.RpcMessageType;
import com.example.rpc.protocol.RpcMessage;
import com.example.rpc.protocol.RpcRequest;
import com.example.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Netty 服务端业务处理器。
 * 负责处理心跳消息和普通 RPC 请求，并返回响应结果。
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private final RpcRequestHandler requestHandler;
    private final byte serializerCode;

    /**
     * 创建服务端处理器。
     *
     * @param requestHandler 请求处理器
     * @param serializerCode 序列化编码
     */
    public RpcServerHandler(RpcRequestHandler requestHandler, byte serializerCode) {
        this.requestHandler = requestHandler;
        this.serializerCode = serializerCode;
    }

    /**
     * 处理服务端收到的 RPC 消息。
     *
     * @param ctx Netty 上下文
     * @param msg RPC 消息
     */
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) {
        if (msg.getMessageType() == RpcMessageType.HEARTBEAT_REQUEST) {
            RpcMessage heartbeat = new RpcMessage();
            heartbeat.setMessageType(RpcMessageType.HEARTBEAT_RESPONSE);
            heartbeat.setCodec(serializerCode);
            heartbeat.setCompress((byte) 0);
            heartbeat.setRequestId(msg.getRequestId());
            heartbeat.setData(RpcConstants.HEARTBEAT_PONG);
            ctx.writeAndFlush(heartbeat).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            return;
        }
        RpcRequest request = (RpcRequest) msg.getData();
        Object result = requestHandler.handle(request);
        RpcResponse response = RpcResponse.success(request.getRequestId(), result);
        RpcMessage responseMessage = new RpcMessage();
        responseMessage.setMessageType(RpcMessageType.RESPONSE);
        responseMessage.setCodec(serializerCode);
        responseMessage.setCompress((byte) 0);
        responseMessage.setRequestId(request.getRequestId());
        responseMessage.setData(response);
        ctx.writeAndFlush(responseMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    /**
     * 处理服务端链路异常并关闭连接。
     *
     * @param ctx Netty 上下文
     * @param cause 异常对象
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
