package com.example.rpc.loadbalance;

import com.example.rpc.spi.Spi;

import java.util.List;

@Spi("random")
/**
 * 负载均衡接口。
 * 负责从多个服务地址中挑选出本次调用的目标节点。
 */
public interface LoadBalance {

    /**
     * 从候选地址列表中选择一个目标地址。
     *
     * @param serviceAddresses 可用服务地址列表
     * @param requestKey 请求标识，可用于一致性哈希等算法
     * @return 被选中的服务地址
     */
    String select(List<String> serviceAddresses, String requestKey);
}
