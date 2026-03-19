package com.example.rpc.cluster;

import com.example.rpc.exception.RpcException;

import java.util.concurrent.Callable;

/**
 * 快速失败策略。
 * 调用异常时立即抛出，不做任何重试。
 */
public class FailFastClusterInvoker implements ClusterInvoker {

    /**
     * 执行调用并在失败时直接抛出异常。
     *
     * @param callable 远程调用逻辑
     * @param <T> 返回值类型
     * @return 调用结果
     */
    public <T> T invoke(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            if (e instanceof RpcException) {
                throw (RpcException) e;
            }
            throw new RpcException(RpcException.ExceptionCode.INVOKE_EXCEPTION, "Fail fast invoke failed", e);
        }
    }
}
