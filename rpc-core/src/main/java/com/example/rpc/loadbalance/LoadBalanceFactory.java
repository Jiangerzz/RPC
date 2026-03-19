package com.example.rpc.loadbalance;

import com.example.rpc.constant.RpcConstants;
import com.example.rpc.spi.SpiLoader;

/**
 * 负载均衡工厂。
 * 通过 SPI 按名称返回具体的负载均衡实现。
 */
public final class LoadBalanceFactory {

    private LoadBalanceFactory() {
    }

    /**
     * 获取负载均衡实现。
     *
     * @param name 负载均衡名称
     * @return 负载均衡实例
     */
    public static LoadBalance getLoadBalance(String name) {
        return SpiLoader.getInstance(LoadBalance.class, name == null ? RpcConstants.DEFAULT_LOAD_BALANCE : name);
    }
}
