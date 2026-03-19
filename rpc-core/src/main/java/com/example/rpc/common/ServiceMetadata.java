package com.example.rpc.common;

import java.io.Serializable;

/**
 * 服务元数据对象。
 * 用于描述一个服务实例的基本信息，供注册和发现阶段使用。
 */
public class ServiceMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serviceInterface;
    private String serviceImpl;
    private String host;
    private int port;
    private int weight = 1;

    /**
     * 创建空的服务元数据对象。
     */
    public ServiceMetadata() {
    }

    /**
     * 使用基础信息创建服务元数据对象。
     *
     * @param serviceInterface 服务接口名
     * @param serviceImpl 服务实现类名
     * @param host 服务主机
     * @param port 服务端口
     */
    public ServiceMetadata(String serviceInterface, String serviceImpl, String host, int port) {
        this.serviceInterface = serviceInterface;
        this.serviceImpl = serviceImpl;
        this.host = host;
        this.port = port;
    }

    /**
     * 获取服务接口全限定名。
     *
     * @return 服务接口名
     */
    public String getServiceInterface() {
        return serviceInterface;
    }

    /**
     * 设置服务接口全限定名。
     *
     * @param serviceInterface 服务接口名
     */
    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    /**
     * 获取服务实现类全限定名。
     *
     * @return 服务实现类名
     */
    public String getServiceImpl() {
        return serviceImpl;
    }

    /**
     * 设置服务实现类全限定名。
     *
     * @param serviceImpl 服务实现类名
     */
    public void setServiceImpl(String serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    /**
     * 获取服务主机地址。
     *
     * @return 主机地址
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置服务主机地址。
     *
     * @param host 主机地址
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取服务端口。
     *
     * @return 服务端口
     */
    public int getPort() {
        return port;
    }

    /**
     * 设置服务端口。
     *
     * @param port 服务端口
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 获取服务权重。
     *
     * @return 服务权重
     */
    public int getWeight() {
        return weight;
    }

    /**
     * 设置服务权重。
     *
     * @param weight 服务权重
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * 将主机和端口拼接为地址字符串。
     *
     * @return host:port 格式地址
     */
    public String toAddress() {
        return host + ":" + port;
    }
}
