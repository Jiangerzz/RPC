package com.example.rpc.loadbalance;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 轮询负载均衡测试。
 * 验证地址选择顺序符合轮询预期。
 */
public class RoundRobinLoadBalanceTest {

    /**
     * 验证轮询策略会按顺序依次返回节点。
     */
    @Test
    public void shouldSelectNodesInOrder() {
        List<String> addresses = Arrays.asList("127.0.0.1:8081", "127.0.0.1:8082", "127.0.0.1:8083");
        RoundRobinLoadBalance loadBalance = new RoundRobinLoadBalance();

        Assert.assertEquals("127.0.0.1:8081", loadBalance.select(addresses, "1"));
        Assert.assertEquals("127.0.0.1:8082", loadBalance.select(addresses, "2"));
        Assert.assertEquals("127.0.0.1:8083", loadBalance.select(addresses, "3"));
        Assert.assertEquals("127.0.0.1:8081", loadBalance.select(addresses, "4"));
    }
}
