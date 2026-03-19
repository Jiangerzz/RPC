package com.example.rpc.codec;

import com.example.rpc.constant.RpcConstants;
import com.example.rpc.protocol.RpcMessage;
import com.example.rpc.serialize.Serializer;
import com.example.rpc.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RPC 消息编码器。
 * 负责将统一消息对象编码为自定义二进制协议格式。
 */
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    /**
     * 将消息头和消息体写入 ByteBuf。
     *
     * @param ctx Netty 上下文
     * @param msg 待编码消息
     * @param out 输出缓冲区
     */
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) {
        Serializer serializer = SerializerFactory.getByCode(msg.getCodec());
        byte[] body = serializer.serialize(msg.getData());
        out.writeShort(RpcConstants.MAGIC);
        out.writeByte(RpcConstants.VERSION);
        out.writeByte(msg.getMessageType());
        out.writeByte(msg.getCodec());
        out.writeByte(msg.getCompress());
        out.writeInt(Integer.parseInt(msg.getRequestId()));
        out.writeInt(body.length);
        out.writeBytes(body);
    }
}
