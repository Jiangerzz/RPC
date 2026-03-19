package com.example.rpc.loadbalance;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡实现。
 * 通过构建虚拟节点哈希环，尽量减少节点上下线时的请求漂移。
 */
public class ConsistentHashLoadBalance implements LoadBalance {

    private static final int VIRTUAL_NODES = 32;

    /**
     * 根据请求标识在哈希环上选择目标地址。
     *
     * @param serviceAddresses 可用服务地址列表
     * @param requestKey 请求标识
     * @return 被选中的服务地址
     */
    public String select(List<String> serviceAddresses, String requestKey) {
        if (serviceAddresses == null || serviceAddresses.isEmpty()) {
            return null;
        }
        SortedMap<Integer, String> ring = new TreeMap<Integer, String>();
        for (String address : serviceAddresses) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                ring.put(hash(address + "#" + i), address);
            }
        }
        int key = hash(requestKey == null ? "default" : requestKey);
        SortedMap<Integer, String> tailMap = ring.tailMap(key);
        Integer nodeKey = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        return ring.get(nodeKey);
    }

    /**
     * 计算字符串对应的哈希值。
     *
     * @param value 待计算的字符串
     * @return 哈希结果
     */
    private int hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            int result = ((bytes[3] & 0xff) << 24)
                    | ((bytes[2] & 0xff) << 16)
                    | ((bytes[1] & 0xff) << 8)
                    | (bytes[0] & 0xff);
            return result & 0x7fffffff;
        } catch (NoSuchAlgorithmException e) {
            return Math.abs(value.hashCode());
        }
    }
}
