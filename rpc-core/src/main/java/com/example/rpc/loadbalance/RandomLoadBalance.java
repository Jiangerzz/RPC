package com.example.rpc.loadbalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机负载均衡实现。
 * 每次请求从地址列表中随机选择一个节点。
 */
public class RandomLoadBalance implements LoadBalance {

    /**
     * 随机返回一个服务地址。
     *
     * @param serviceAddresses 可用服务地址列表
     * @param requestKey 请求标识
     * @return 被选中的服务地址
     */
    public String select(List<String> serviceAddresses, String requestKey) {
        if (serviceAddresses == null || serviceAddresses.isEmpty()) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(serviceAddresses.size());
        return serviceAddresses.get(index);
    }
}
