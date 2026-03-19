package com.example.rpc.example.consumer;

import com.example.rpc.config.RpcClientProperties;
import com.example.rpc.example.api.HelloService;
import com.example.rpc.proxy.RpcClientProxy;

/**
 * 示例消费者启动类。
 * 用于创建客户端代理并发起一次远程调用。
 */
public class ConsumerApplication {

    /**
     * 启动 Consumer 示例。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        RpcClientProperties properties = new RpcClientProperties();
        properties.setSerializer("jdk");
        properties.setLoadBalance("random");
        properties.setCluster("failFast");
        properties.setDirectServerAddress("127.0.0.1:8088");

        RpcClientProxy proxy = new RpcClientProxy(properties);
        HelloService helloService = proxy.getProxy(HelloService.class);
        String result = helloService.sayHello("Codex");
        System.out.println(result);
    }
}
