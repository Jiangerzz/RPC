package com.example.rpc.registry;

import com.example.rpc.common.ServiceMetadata;
import com.example.rpc.spi.Spi;

import java.util.List;

@Spi("zk")
/**
 * 注册中心接口。
 * 定义服务注册、注销、发现和资源释放的统一能力。
 */
public interface Registry {

    /**
     * 注册服务实例。
     *
     * @param metadata 服务元数据
     */
    void register(ServiceMetadata metadata);

    /**
     * 注销服务实例。
     *
     * @param metadata 服务元数据
     */
    void unregister(ServiceMetadata metadata);

    /**
     * 查询指定服务的全部可用地址。
     *
     * @param serviceName 服务名称
     * @return 可用地址列表
     */
    List<String> discover(String serviceName);

    /**
     * 释放注册中心相关资源。
     */
    void destroy();
}
