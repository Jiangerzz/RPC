package com.example.rpc.config;

/**
 * 客户端配置对象。
 * 统一管理消费者侧的序列化、负载均衡、超时和地址配置。
 */
public class RpcClientProperties {

    private String serializer = "jdk";
    private String loadBalance = "random";
    private String cluster = "failFast";
    private String registryAddress = "127.0.0.1:2181";
    private String directServerAddress;
    private long timeoutMillis = 3000L;

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
     * 获取负载均衡策略名称。
     *
     * @return 负载均衡策略
     */
    public String getLoadBalance() {
        return loadBalance;
    }

    /**
     * 设置负载均衡策略名称。
     *
     * @param loadBalance 负载均衡策略
     */
    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    /**
     * 获取集群容错策略名称。
     *
     * @return 容错策略
     */
    public String getCluster() {
        return cluster;
    }

    /**
     * 设置集群容错策略名称。
     *
     * @param cluster 容错策略
     */
    public void setCluster(String cluster) {
        this.cluster = cluster;
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
     * 获取直连模式下的服务端地址。
     *
     * @return 服务端地址
     */
    public String getDirectServerAddress() {
        return directServerAddress;
    }

    /**
     * 设置直连模式下的服务端地址。
     *
     * @param directServerAddress 服务端地址
     */
    public void setDirectServerAddress(String directServerAddress) {
        this.directServerAddress = directServerAddress;
    }

    /**
     * 获取客户端请求超时时间。
     *
     * @return 超时时间，单位毫秒
     */
    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    /**
     * 设置客户端请求超时时间。
     *
     * @param timeoutMillis 超时时间，单位毫秒
     */
    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }
}
