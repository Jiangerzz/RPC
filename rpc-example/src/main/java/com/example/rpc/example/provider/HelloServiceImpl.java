package com.example.rpc.example.provider;

import com.example.rpc.example.api.HelloService;

/**
 * 示例服务实现类。
 * 在 Provider 端被注册并通过 RPC 对外暴露。
 */
public class HelloServiceImpl implements HelloService {

    /**
     * 返回包含调用参数的问候语。
     *
     * @param name 名字
     * @return 问候语字符串
     */
    public String sayHello(String name) {
        return "Hello, " + name + " from custom RPC";
    }
}
