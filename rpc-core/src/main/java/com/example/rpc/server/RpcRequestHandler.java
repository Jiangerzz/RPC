package com.example.rpc.server;

import com.example.rpc.exception.RpcException;
import com.example.rpc.protocol.RpcRequest;

import java.lang.reflect.Method;

/**
 * RPC 请求处理器。
 * 负责根据请求信息定位服务方法并通过反射执行。
 */
public class RpcRequestHandler {

    private final ServiceProvider serviceProvider;

    /**
     * 创建请求处理器。
     *
     * @param serviceProvider 本地服务容器
     */
    public RpcRequestHandler(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    /**
     * 执行一次 RPC 请求。
     *
     * @param request RPC 请求对象
     * @return 方法执行结果
     */
    public Object handle(RpcRequest request) {
        Object service = serviceProvider.getService(request.getServiceName());
        if (service == null) {
            throw new RpcException(RpcException.ExceptionCode.INVOKE_EXCEPTION, "Service not found: " + request.getServiceName());
        }
        try {
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            return method.invoke(service, request.getParameters());
        } catch (Exception e) {
            throw new RpcException(RpcException.ExceptionCode.INVOKE_EXCEPTION,
                    "Failed to invoke " + request.getServiceName() + "#" + request.getMethodName(), e);
        }
    }
}
