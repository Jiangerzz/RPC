package com.example.rpc.serialize;

import com.alibaba.fastjson2.JSON;
import com.example.rpc.exception.RpcException;

import java.nio.charset.StandardCharsets;

/**
 * JSON 序列化实现。
 * 适合调试和排查问题，报文可读性较好。
 */
public class JsonSerializer implements Serializer {

    /**
     * 返回 JSON 序列化对应的协议编码。
     *
     * @return 协议编码
     */
    public byte getCode() {
        return 2;
    }

    /**
     * 将对象转换为 JSON 字节数组。
     *
     * @param obj 待序列化对象
     * @return 字节数组
     */
    public byte[] serialize(Object obj) {
        try {
            return JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RpcException(RpcException.ExceptionCode.SERIALIZE_EXCEPTION, "JSON serialize failed", e);
        }
    }

    /**
     * 将 JSON 字节数组反序列化为目标对象。
     *
     * @param bytes 字节数组
     * @param clazz 目标类型
     * @param <T> 泛型类型
     * @return 反序列化对象
     */
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            return JSON.parseObject(new String(bytes, StandardCharsets.UTF_8), clazz);
        } catch (Exception e) {
            throw new RpcException(RpcException.ExceptionCode.SERIALIZE_EXCEPTION, "JSON deserialize failed", e);
        }
    }
}
