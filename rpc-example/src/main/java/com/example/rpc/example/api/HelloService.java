package com.example.rpc.example.api;

/**
 * 示例服务接口。
 * 用于演示消费者如何像调用本地方法一样调用远程服务。
 */
public interface HelloService {

    /**
     * 返回一个问候语。
     *
     * @param name 调用方传入的名字
     * @return 问候语字符串
     */
    String sayHello(String name);
}
