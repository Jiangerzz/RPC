package com.example.rpc.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地服务提供者容器。
 * 在服务端维护“服务名 -> 服务实现对象”的映射关系。
 */
public class ServiceProvider {

    private final Map<String, Object> serviceMap = new ConcurrentHashMap<String, Object>();

    /**
     * 注册一个本地服务实现。
     *
     * @param serviceName 服务名称
     * @param service 服务实现对象
     */
    public void addService(String serviceName, Object service) {
        serviceMap.put(serviceName, service);
    }

    /**
     * 获取指定名称的本地服务实现。
     *
     * @param serviceName 服务名称
     * @return 服务实现对象
     */
    public Object getService(String serviceName) {
        return serviceMap.get(serviceName);
    }
}
