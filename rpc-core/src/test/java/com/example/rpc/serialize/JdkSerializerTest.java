package com.example.rpc.serialize;

import com.example.rpc.protocol.RpcRequest;
import org.junit.Assert;
import org.junit.Test;

/**
 * JDK 序列化测试。
 * 验证请求对象在序列化和反序列化后字段保持一致。
 */
public class JdkSerializerTest {

    /**
     * 验证请求对象可以被正确序列化并恢复。
     */
    @Test
    public void shouldSerializeAndDeserializeRequest() {
        RpcRequest request = new RpcRequest();
        request.setRequestId("1");
        request.setServiceName("demoService");
        request.setMethodName("ping");
        request.setParamTypes(new Class<?>[]{String.class});
        request.setParameters(new Object[]{"ok"});

        Serializer serializer = new JdkSerializer();
        byte[] bytes = serializer.serialize(request);
        RpcRequest decoded = serializer.deserialize(bytes, RpcRequest.class);

        Assert.assertEquals(request.getRequestId(), decoded.getRequestId());
        Assert.assertEquals(request.getServiceName(), decoded.getServiceName());
        Assert.assertEquals(request.getMethodName(), decoded.getMethodName());
        Assert.assertEquals("ok", decoded.getParameters()[0]);
    }
}
