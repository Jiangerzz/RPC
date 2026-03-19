package com.example.rpc.example.nativeio;

import java.io.Serializable;

/**
 * 原生 Socket demo 的响应对象。
 * 用于将服务端执行结果或异常信息返回给客户端。
 */
public class SocketRpcResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private Object data;
    private String message;

    /**
     * 创建成功响应。
     *
     * @param data 返回数据
     * @return 响应对象
     */
    public static SocketRpcResponse success(Object data) {
        SocketRpcResponse response = new SocketRpcResponse();
        response.setSuccess(true);
        response.setData(data);
        response.setMessage("OK");
        return response;
    }

    /**
     * 创建失败响应。
     *
     * @param message 失败信息
     * @return 响应对象
     */
    public static SocketRpcResponse fail(String message) {
        SocketRpcResponse response = new SocketRpcResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    /**
     * 获取是否成功。
     *
     * @return true 表示成功
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置是否成功。
     *
     * @param success true 表示成功
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 获取返回数据。
     *
     * @return 返回数据
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置返回数据。
     *
     * @param data 返回数据
     */
    public void setData(Object data) {
        this.data = data;
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
}
