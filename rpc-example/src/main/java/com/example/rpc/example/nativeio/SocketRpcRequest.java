package com.example.rpc.example.nativeio;

import java.io.Serializable;

/**
 * 原生 Socket demo 的请求对象。
 * 用于把客户端想调用的服务、方法和参数通过对象流传给服务端。
 */
public class SocketRpcRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serviceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    /**
     * 获取服务名称。
     *
     * @return 服务名称
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * 设置服务名称。
     *
     * @param serviceName 服务名称
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * 获取方法名称。
     *
     * @return 方法名称
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 设置方法名称。
     *
     * @param methodName 方法名称
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * 获取参数类型数组。
     *
     * @return 参数类型数组
     */
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    /**
     * 设置参数类型数组。
     *
     * @param parameterTypes 参数类型数组
     */
    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /**
     * 获取参数值数组。
     *
     * @return 参数值数组
     */
    public Object[] getParameters() {
        return parameters;
    }

    /**
     * 设置参数值数组。
     *
     * @param parameters 参数值数组
     */
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
