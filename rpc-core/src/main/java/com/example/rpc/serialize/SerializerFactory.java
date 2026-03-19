package com.example.rpc.serialize;

import com.example.rpc.constant.RpcConstants;
import com.example.rpc.spi.SpiLoader;

/**
 * 序列化工厂。
 * 负责按名称或按协议编码获取序列化实现。
 */
public final class SerializerFactory {

    private SerializerFactory() {
    }

    /**
     * 根据名称获取序列化实现。
     *
     * @param name 序列化名称
     * @return 序列化实现
     */
    public static Serializer getSerializer(String name) {
        return SpiLoader.getInstance(Serializer.class, name == null ? RpcConstants.DEFAULT_SERIALIZER : name);
    }

    /**
     * 根据协议编码获取序列化实现。
     *
     * @param code 序列化编码
     * @return 序列化实现
     */
    public static Serializer getByCode(byte code) {
        if (code == 2) {
            return getSerializer("json");
        }
        return getSerializer("jdk");
    }
}
