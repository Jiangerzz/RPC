package com.example.rpc.protocol;

import java.io.Serializable;

/**
 * RPC 请求对象。
 * 封装一次远程方法调用所需要的全部上下文信息。
 */
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String requestId;
    private String serviceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] parameters;

    /**
     * 获取请求 ID。
     *
     * @return 请求 ID
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * 设置请求 ID。
     *
     * @param requestId 请求 ID
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

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
    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    /**
     * 设置参数类型数组。
     *
     * @param paramTypes 参数类型数组
     */
    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
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
