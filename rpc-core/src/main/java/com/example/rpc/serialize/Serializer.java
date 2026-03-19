package com.example.rpc.serialize;

import com.example.rpc.spi.Spi;

@Spi("jdk")
/**
 * 序列化接口。
 * 统一定义对象与字节数组之间的转换能力。
 */
public interface Serializer {

    /**
     * 返回当前序列化实现的协议编码。
     *
     * @return 序列化编码
     */
    byte getCode();

    /**
     * 将对象序列化为字节数组。
     *
     * @param obj 待序列化对象
     * @return 序列化结果
     */
    byte[] serialize(Object obj);

    /**
     * 将字节数组反序列化为指定类型对象。
     *
     * @param bytes 字节数组
     * @param clazz 目标类型
     * @param <T> 目标泛型
     * @return 反序列化后的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
