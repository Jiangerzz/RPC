package com.example.rpc.config;

/**
 * 服务端配置对象。
 * 用于集中管理监听地址、序列化方式和注册中心相关参数。
 */
public class RpcServerProperties {

    private String host = "127.0.0.1";
    private int port = 8088;
    private String serializer = "jdk";
    private String registryAddress = "127.0.0.1:2181";
    private boolean registerToRegistry;

    /**
     * 获取服务端主机地址。
     *
     * @return 主机地址
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置服务端主机地址。
     *
     * @param host 主机地址
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取服务端监听端口。
     *
     * @return 监听端口
     */
    public int getPort() {
        return port;
    }

    /**
     * 设置服务端监听端口。
     *
     * @param port 监听端口
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 获取序列化方式名称。
     *
     * @return 序列化方式
     */
    public String getSerializer() {
        return serializer;
    }

    /**
     * 设置序列化方式名称。
     *
     * @param serializer 序列化方式
     */
    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }

    /**
     * 获取注册中心地址。
     *
     * @return 注册中心地址
     */
    public String getRegistryAddress() {
        return registryAddress;
    }

    /**
     * 设置注册中心地址。
     *
     * @param registryAddress 注册中心地址
     */
    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    /**
     * 判断是否需要在启动时注册到注册中心。
     *
     * @return true 表示需要注册
     */
    public boolean isRegisterToRegistry() {
        return registerToRegistry;
    }

    /**
     * 设置是否需要注册到注册中心。
     *
     * @param registerToRegistry true 表示需要注册
     */
    public void setRegisterToRegistry(boolean registerToRegistry) {
        this.registerToRegistry = registerToRegistry;
    }
}
