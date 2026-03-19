package com.example.rpc.serialize;

import com.example.rpc.exception.RpcException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * JDK 原生序列化实现。
 * 适合快速验证 RPC 主链路，但性能和体积都不是最优。
 */
public class JdkSerializer implements Serializer {

    /**
     * 返回 JDK 序列化对应的协议编码。
     *
     * @return 协议编码
     */
    public byte getCode() {
        return 1;
    }

    /**
     * 使用 JDK 对象流完成序列化。
     *
     * @param obj 待序列化对象
     * @return 字节数组
     */
    public byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RpcException(RpcException.ExceptionCode.SERIALIZE_EXCEPTION, "JDK serialize failed", e);
        }
    }

    /**
     * 使用 JDK 对象流完成反序列化。
     *
     * @param bytes 字节数组
     * @param clazz 目标类型
     * @param <T> 泛型类型
     * @return 反序列化对象
     */
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object object = objectInputStream.readObject();
            return (T) object;
        } catch (IOException e) {
            throw new RpcException(RpcException.ExceptionCode.SERIALIZE_EXCEPTION, "JDK deserialize failed", e);
        } catch (ClassNotFoundException e) {
            throw new RpcException(RpcException.ExceptionCode.SERIALIZE_EXCEPTION, "Deserialized class not found", e);
        }
    }
}
