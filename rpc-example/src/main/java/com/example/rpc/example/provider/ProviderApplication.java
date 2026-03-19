package com.example.rpc.example.provider;

import com.example.rpc.config.RpcServerProperties;
import com.example.rpc.server.RpcServer;
import com.example.rpc.example.api.HelloService;

/**
 * 示例服务提供者启动类。
 * 用于启动 Netty 服务端并暴露 HelloService 服务。
 */
public class ProviderApplication {

    /**
     * 启动 Provider 示例。
     *
     * @param args 命令行参数
     * @throws Exception 启动过程中发生异常时抛出
     */
    public static void main(String[] args) throws Exception {
        RpcServerProperties properties = new RpcServerProperties();
        properties.setHost("127.0.0.1");
        properties.setPort(8088);
        properties.setSerializer("jdk");

        RpcServer rpcServer = new RpcServer(properties);
        rpcServer.addService(HelloService.class, new HelloServiceImpl());
        rpcServer.start();
    }
}
