package com.example.rpc.server;

import com.example.rpc.common.ServiceMetadata;
import com.example.rpc.registry.Registry;

/**
 * 服务注册器。
 * 负责将服务端暴露的服务信息写入注册中心。
 */
public class ServiceRegistry {

    private final Registry registry;

    /**
     * 创建服务注册器。
     *
     * @param registry 注册中心实现
     */
    public ServiceRegistry(Registry registry) {
        this.registry = registry;
    }

    /**
     * 将一个服务接口和实现注册到注册中心。
     *
     * @param serviceInterface 服务接口
     * @param serviceImpl 服务实现
     * @param host 服务主机
     * @param port 服务端口
     * @param <T> 服务泛型
     */
    public <T> void registerService(Class<T> serviceInterface, T serviceImpl, String host, int port) {
        ServiceMetadata metadata = new ServiceMetadata(serviceInterface.getName(), serviceImpl.getClass().getName(), host, port);
        registry.register(metadata);
    }
}
