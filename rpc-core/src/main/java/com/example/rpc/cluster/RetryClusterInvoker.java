package com.example.rpc.cluster;

import com.example.rpc.exception.RpcException;

import java.util.concurrent.Callable;

/**
 * 重试策略。
 * 当调用失败时会按预设次数重复执行同一段调用逻辑。
 */
public class RetryClusterInvoker implements ClusterInvoker {

    private final int retries;

    /**
     * 使用默认重试次数创建策略实例。
     */
    public RetryClusterInvoker() {
        this(2);
    }

    /**
     * 使用指定重试次数创建策略实例。
     *
     * @param retries 失败后的重试次数
     */
    public RetryClusterInvoker(int retries) {
        this.retries = retries;
    }

    /**
     * 执行调用并在失败时进行重试。
     *
     * @param callable 远程调用逻辑
     * @param <T> 返回值类型
     * @return 调用结果
     */
    public <T> T invoke(Callable<T> callable) {
        RpcException last = null;
        for (int i = 0; i <= retries; i++) {
            try {
                return callable.call();
            } catch (Exception e) {
                last = e instanceof RpcException
                        ? (RpcException) e
                        : new RpcException(RpcException.ExceptionCode.INVOKE_EXCEPTION, "Retry invoke failed", e);
            }
        }
        throw last;
    }
}
