package com.example.rpc.cluster;

import com.example.rpc.spi.Spi;

import java.util.concurrent.Callable;

@Spi("failFast")
/**
 * 集群调用器接口。
 * 用于在真正发起远程调用前套上一层容错策略，例如快速失败或重试。
 */
public interface ClusterInvoker {

    /**
     * 执行一次带容错能力的调用。
     *
     * @param callable 真实的远程调用逻辑
     * @param <T> 返回值类型
     * @return 调用结果
     */
    <T> T invoke(Callable<T> callable);
}
