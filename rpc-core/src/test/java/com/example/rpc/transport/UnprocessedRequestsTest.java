package com.example.rpc.transport;

import com.example.rpc.protocol.RpcResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 未完成请求管理器测试。
 * 验证响应返回后能正确完成对应的 Future。
 */
public class UnprocessedRequestsTest {

    /**
     * 验证请求与响应可以按 requestId 正确匹配。
     */
    @Test
    public void shouldCompletePendingResponse() throws Exception {
        UnprocessedRequests requests = new UnprocessedRequests();
        CompletableFuture<RpcResponse> future = new CompletableFuture<RpcResponse>();
        requests.put("100", future);

        RpcResponse response = RpcResponse.success("100", "ok");
        requests.complete(response);

        Assert.assertEquals("ok", future.get().getData());
    }
}
