package com.example.rpc.protocol;

import java.io.Serializable;

/**
 * RPC 响应对象。
 * 用于返回远程调用结果、状态码和异常信息。
 */
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String requestId;
    private Object data;
    private int code;
    private String message;
    private Throwable exception;

    /**
     * 创建成功响应。
     *
     * @param requestId 请求 ID
     * @param data 返回数据
     * @return 成功响应对象
     */
    public static RpcResponse success(String requestId, Object data) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(requestId);
        response.setCode(200);
        response.setMessage("OK");
        response.setData(data);
        return response;
    }

    /**
     * 创建失败响应。
     *
     * @param requestId 请求 ID
     * @param message 响应消息
     * @param exception 异常对象
     * @return 失败响应对象
     */
    public static RpcResponse fail(String requestId, String message, Throwable exception) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(requestId);
        response.setCode(500);
        response.setMessage(message);
        response.setException(exception);
        return response;
    }

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
     * 获取响应数据。
     *
     * @return 响应数据
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置响应数据。
     *
     * @param data 响应数据
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 获取响应码。
     *
     * @return 响应码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置响应码。
     *
     * @param code 响应码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取响应消息。
     *
     * @return 响应消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置响应消息。
     *
     * @param message 响应消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取异常对象。
     *
     * @return 异常对象
     */
    public Throwable getException() {
        return exception;
    }

    /**
     * 设置异常对象。
     *
     * @param exception 异常对象
     */
    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
