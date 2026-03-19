package com.example.rpc.discovery;

import com.example.rpc.registry.Registry;

import java.util.List;

/**
 * 服务发现器。
 * 对注册中心的发现能力做一层简单封装，便于客户端调用。
 */
public class ServiceDiscovery {

    private final Registry registry;

    /**
     * 创建服务发现器。
     *
     * @param registry 注册中心实现
     */
    public ServiceDiscovery(Registry registry) {
        this.registry = registry;
    }

    /**
     * 获取某个服务名对应的全部可用地址。
     *
     * @param serviceName 服务名称
     * @return 可用地址列表
     */
    public List<String> discoverService(String serviceName) {
        return registry.discover(serviceName);
    }
}
