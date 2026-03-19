package com.example.rpc.codec;

import com.example.rpc.constant.RpcConstants;
import com.example.rpc.constant.RpcMessageType;
import com.example.rpc.protocol.RpcMessage;
import com.example.rpc.protocol.RpcRequest;
import com.example.rpc.protocol.RpcResponse;
import com.example.rpc.serialize.Serializer;
import com.example.rpc.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * RPC 消息解码器。
 * 按自定义协议头读取报文，并恢复为统一的消息对象。
 */
public class RpcMessageDecoder extends ByteToMessageDecoder {

    /**
     * 从字节流中解码 RPC 消息。
     *
     * @param ctx Netty 上下文
     * @param in 输入缓冲区
     * @param out 解码后的对象集合
     */
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < RpcConstants.HEADER_LENGTH) {
            return;
        }
        in.markReaderIndex();
        short magic = in.readShort();
        if (magic != RpcConstants.MAGIC) {
            throw new IllegalStateException("Unknown magic number");
        }
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new IllegalStateException("Protocol version mismatch");
        }
        byte messageType = in.readByte();
        byte codec = in.readByte();
        byte compress = in.readByte();
        String requestId = String.valueOf(in.readInt());
        int fullLength = in.readInt();
        if (in.readableBytes() < fullLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] body = new byte[fullLength];
        in.readBytes(body);
        Serializer serializer = SerializerFactory.getByCode(codec);
        RpcMessage message = new RpcMessage();
        message.setMessageType(messageType);
        message.setCodec(codec);
        message.setCompress(compress);
        message.setRequestId(requestId);
        if (messageType == RpcMessageType.REQUEST) {
            message.setData(serializer.deserialize(body, RpcRequest.class));
        } else if (messageType == RpcMessageType.RESPONSE) {
            message.setData(serializer.deserialize(body, RpcResponse.class));
        } else {
            message.setData(serializer.deserialize(body, String.class));
        }
        out.add(message);
    }
}
