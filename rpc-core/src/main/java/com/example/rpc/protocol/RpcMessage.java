package com.example.rpc.protocol;

import java.io.Serializable;

/**
 * 统一 RPC 消息对象。
 * 既可以承载请求，也可以承载响应和心跳数据。
 */
public class RpcMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte messageType;
    private byte codec;
    private byte compress;
    private String requestId;
    private Object data;

    /**
     * 获取消息类型。
     *
     * @return 消息类型
     */
    public byte getMessageType() {
        return messageType;
    }

    /**
     * 设置消息类型。
     *
     * @param messageType 消息类型
     */
    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    /**
     * 获取序列化编码。
     *
     * @return 序列化编码
     */
    public byte getCodec() {
        return codec;
    }

    /**
     * 设置序列化编码。
     *
     * @param codec 序列化编码
     */
    public void setCodec(byte codec) {
        this.codec = codec;
    }

    /**
     * 获取压缩编码。
     *
     * @return 压缩编码
     */
    public byte getCompress() {
        return compress;
    }

    /**
     * 设置压缩编码。
     *
     * @param compress 压缩编码
     */
    public void setCompress(byte compress) {
        this.compress = compress;
    }

    /**
     * 获取请求 ID。
     *
     * @return 请求 ID
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * 设置请求 ID。
     *
     * @param requestId 请求 ID
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * 获取消息体数据。
     *
     * @return 消息体对象
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置消息体数据。
     *
     * @param data 消息体对象
     */
    public void setData(Object data) {
        this.data = data;
    }
}
