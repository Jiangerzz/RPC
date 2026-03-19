package com.example.rpc.constant;

/**
 * RPC 框架全局常量定义。
 * 这里集中维护协议、默认配置和注册中心路径等基础常量。
 */
public final class RpcConstants {

    public static final short MAGIC = (short) 0xcafe;
    public static final byte VERSION = 1;
    public static final int HEADER_LENGTH = 14;
    public static final String DEFAULT_SERIALIZER = "jdk";
    public static final String DEFAULT_LOAD_BALANCE = "random";
    public static final String DEFAULT_CLUSTER = "failFast";
    public static final String DEFAULT_REGISTRY = "zk";
    public static final String HEARTBEAT_PING = "PING";
    public static final String HEARTBEAT_PONG = "PONG";
    public static final String REGISTRY_ROOT_PATH = "/rpc";

    private RpcConstants() {
    }
}
