package com.example.rpc.proxy;

import com.example.rpc.cluster.ClusterInvoker;
import com.example.rpc.cluster.ClusterInvokerFactory;
import com.example.rpc.config.RpcClientProperties;
import com.example.rpc.discovery.ServiceDiscovery;
import com.example.rpc.exception.RpcException;
import com.example.rpc.loadbalance.LoadBalance;
import com.example.rpc.loadbalance.LoadBalanceFactory;
import com.example.rpc.protocol.RpcRequest;
import com.example.rpc.protocol.RpcResponse;
import com.example.rpc.registry.Registry;
import com.example.rpc.registry.RegistryFactory;
import com.example.rpc.transport.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * RPC 客户端动态代理。
 * 对外暴露本地接口代理，内部负责组装请求并发起远程调用。
 */
public class RpcClientProxy implements InvocationHandler {

    private final RpcClient client;
    private final RpcClientProperties properties;
    private final LoadBalance loadBalance;
    private final ClusterInvoker clusterInvoker;
    private final ServiceDiscovery serviceDiscovery;

    /**
     * 根据客户端配置创建代理对象工厂。
     *
     * @param properties 客户端配置
     */
    public RpcClientProxy(RpcClientProperties properties) {
        this.properties = properties;
        this.client = new RpcClient(properties);
        this.loadBalance = LoadBalanceFactory.getLoadBalance(properties.getLoadBalance());
        this.clusterInvoker = ClusterInvokerFactory.getClusterInvoker(properties.getCluster());
        if (properties.getDirectServerAddress() == null || properties.getDirectServerAddress().trim().isEmpty()) {
            Registry registry = new com.example.rpc.registry.ZkRegistry(properties.getRegistryAddress());
            this.serviceDiscovery = new ServiceDiscovery(registry);
        } else {
            this.serviceDiscovery = null;
        }
    }

    /**
     * 为指定服务接口创建 JDK 动态代理对象。
     *
     * @param serviceClass 服务接口类型
     * @param <T> 接口泛型
     * @return 动态代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, this);
    }

    /**
     * 拦截接口方法调用并转换为 RPC 请求。
     *
     * @param proxy 代理对象
     * @param method 被调用的方法
     * @param args 调用参数
     * @return 远程调用结果
     */
    public Object invoke(Object proxy, final Method method, final Object[] args) {
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Exception e) {
                throw new RpcException(RpcException.ExceptionCode.INVOKE_EXCEPTION, "Local method invoke failed", e);
            }
        }
        return clusterInvoker.invoke(new java.util.concurrent.Callable<Object>() {
            public Object call() {
                RpcRequest request = new RpcRequest();
                request.setRequestId(String.valueOf(Math.abs(UUID.randomUUID().hashCode())));
                request.setServiceName(method.getDeclaringClass().getName());
                request.setMethodName(method.getName());
                request.setParamTypes(method.getParameterTypes());
                request.setParameters(args);
                String address = resolveAddress(request.getServiceName(), request.getRequestId());
                RpcResponse response = client.sendRequest(address, request);
                if (response.getCode() != 200) {
                    throw new RpcException(RpcException.ExceptionCode.INVOKE_EXCEPTION, response.getMessage(), response.getException());
                }
                return response.getData();
            }
        });
    }

    /**
     * 解析本次调用应该连接的目标地址。
     *
     * @param serviceName 服务名称
     * @param requestId 请求 ID
     * @return 目标地址
     */
    private String resolveAddress(String serviceName, String requestId) {
        if (properties.getDirectServerAddress() != null && !properties.getDirectServerAddress().trim().isEmpty()) {
            return properties.getDirectServerAddress();
        }
        List<String> addresses = serviceDiscovery == null
                ? Collections.<String>emptyList()
                : serviceDiscovery.discoverService(serviceName);
        if (addresses == null || addresses.isEmpty()) {
            throw new RpcException(RpcException.ExceptionCode.REGISTRY_EXCEPTION, "No service provider found for " + serviceName);
        }
        return loadBalance.select(addresses, requestId);
    }
}
