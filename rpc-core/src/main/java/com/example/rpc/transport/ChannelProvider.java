package com.example.rpc.transport;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接提供者。
 * 负责缓存并复用“服务地址 -> Netty Channel”的映射关系。
 */
public class ChannelProvider {

    private final Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();

    /**
     * 获取某个地址对应的可用连接。
     *
     * @param address 服务地址
     * @return 可用 Channel，不存在时返回 null
     */
    public Channel get(String address) {
        Channel channel = channelMap.get(address);
        if (channel != null && channel.isActive()) {
            return channel;
        }
        return null;
    }

    /**
     * 缓存某个地址对应的连接。
     *
     * @param address 服务地址
     * @param channel 连接对象
     */
    public void set(String address, Channel channel) {
        channelMap.put(address, channel);
    }

    /**
     * 移除某个地址对应的连接缓存。
     *
     * @param address 服务地址
     */
    public void remove(String address) {
        channelMap.remove(address);
    }
}
