package com.example.rpc.constant;

/**
 * RPC 消息类型常量。
 * 用于区分普通请求、普通响应以及心跳报文。
 */
public final class RpcMessageType {

    public static final byte REQUEST = 1;
    public static final byte RESPONSE = 2;
    public static final byte HEARTBEAT_REQUEST = 3;
    public static final byte HEARTBEAT_RESPONSE = 4;

    private RpcMessageType() {
    }
}
