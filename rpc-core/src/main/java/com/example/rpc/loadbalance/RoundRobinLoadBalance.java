package com.example.rpc.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡实现。
 * 按顺序依次选择服务地址，保证请求分发相对均匀。
 */
public class RoundRobinLoadBalance implements LoadBalance {

    private final AtomicInteger index = new AtomicInteger(0);

    /**
     * 使用轮询方式选择服务地址。
     *
     * @param serviceAddresses 可用服务地址列表
     * @param requestKey 请求标识
     * @return 被选中的服务地址
     */
    public String select(List<String> serviceAddresses, String requestKey) {
        if (serviceAddresses == null || serviceAddresses.isEmpty()) {
            return null;
        }
        int next = Math.abs(index.getAndIncrement());
        return serviceAddresses.get(next % serviceAddresses.size());
    }
}
