package com.example.rpc.registry;

import com.example.rpc.constant.RpcConstants;
import com.example.rpc.spi.SpiLoader;

/**
 * 注册中心工厂。
 * 负责通过 SPI 获取具体的注册中心实现。
 */
public final class RegistryFactory {

    private RegistryFactory() {
    }

    /**
     * 根据名称获取注册中心实现。
     *
     * @param name 注册中心名称
     * @return 注册中心实例
     */
    public static Registry getRegistry(String name) {
        return SpiLoader.getInstance(Registry.class, name == null ? RpcConstants.DEFAULT_REGISTRY : name);
    }
}
