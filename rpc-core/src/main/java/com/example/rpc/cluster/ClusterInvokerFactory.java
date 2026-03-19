package com.example.rpc.cluster;

import com.example.rpc.constant.RpcConstants;
import com.example.rpc.spi.SpiLoader;

/**
 * 集群调用器工厂。
 * 负责根据名称从 SPI 中加载对应的容错策略实现。
 */
public final class ClusterInvokerFactory {

    private ClusterInvokerFactory() {
    }

    /**
     * 根据名称获取集群调用器。
     *
     * @param name 策略名称
     * @return 集群调用器实现
     */
    public static ClusterInvoker getClusterInvoker(String name) {
        return SpiLoader.getInstance(ClusterInvoker.class, name == null ? RpcConstants.DEFAULT_CLUSTER : name);
    }
}
