package com.example.rpc.spi;

import com.example.rpc.loadbalance.LoadBalance;
import org.junit.Assert;
import org.junit.Test;

/**
 * SPI 加载器测试。
 * 验证框架能够根据名称正确加载指定实现类。
 */
public class SpiLoaderTest {

    /**
     * 验证 roundRobin 名称能正确映射到轮询实现。
     */
    @Test
    public void shouldLoadRoundRobinImplementation() {
        LoadBalance loadBalance = SpiLoader.getInstance(LoadBalance.class, "roundRobin");
        Assert.assertEquals("RoundRobinLoadBalance", loadBalance.getClass().getSimpleName());
    }
}
