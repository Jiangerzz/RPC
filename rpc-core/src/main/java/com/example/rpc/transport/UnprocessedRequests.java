package com.example.rpc.transport;

import com.example.rpc.protocol.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 未完成请求管理器。
 * 在客户端维护 requestId 与 CompletableFuture 的对应关系。
 */
public class UnprocessedRequests {

    private final Map<String, CompletableFuture<RpcResponse>> pendingRequests =
            new ConcurrentHashMap<String, CompletableFuture<RpcResponse>>();

    /**
     * 注册一个待返回结果的请求。
     *
     * @param requestId 请求 ID
     * @param future 响应 Future
     */
    public void put(String requestId, CompletableFuture<RpcResponse> future) {
        pendingRequests.put(requestId, future);
    }

    /**
     * 根据响应完成对应的 Future。
     *
     * @param response RPC 响应对象
     */
    public void complete(RpcResponse response) {
        CompletableFuture<RpcResponse> future = pendingRequests.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        }
    }

    /**
     * 在请求失败时以异常形式完成对应的 Future。
     *
     * @param requestId 请求 ID
     * @param throwable 异常对象
     */
    public void fail(String requestId, Throwable throwable) {
        CompletableFuture<RpcResponse> future = pendingRequests.remove(requestId);
        if (future != null) {
            future.completeExceptionally(throwable);
        }
    }
}
