package com.example.rpc.exception;

/**
 * RPC 框架统一异常类型。
 * 用于在各层之间传递标准化的异常码和异常信息。
 */
public class RpcException extends RuntimeException {

    private final int code;

    /**
     * 使用异常码和异常信息创建异常。
     *
     * @param code 异常码
     * @param message 异常描述
     */
    public RpcException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 使用异常码、异常信息和原始异常创建异常。
     *
     * @param code 异常码
     * @param message 异常描述
     * @param cause 原始异常
     */
    public RpcException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 返回当前异常对应的异常码。
     *
     * @return 异常码
     */
    public int getCode() {
        return code;
    }

    /**
     * 框架内置异常码定义。
     */
    public static final class ExceptionCode {
        public static final int NETWORK_EXCEPTION = 1001;
        public static final int SERIALIZE_EXCEPTION = 1002;
        public static final int REGISTRY_EXCEPTION = 1003;
        public static final int INVOKE_EXCEPTION = 1004;
        public static final int CONFIG_EXCEPTION = 1005;

        private ExceptionCode() {
        }
    }
}
