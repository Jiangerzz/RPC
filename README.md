# Custom RPC Framework

基于《自定义RPC框架设计文档_优化版》生成的 Java RPC 学习项目，采用 Maven 多模块结构实现一个轻量级、可扩展的 RPC 框架。

项目当前已经具备以下基础能力：

- 基于 Netty 的客户端/服务端通信
- 自定义二进制协议头，支持请求、响应、心跳消息
- JDK 动态代理发起远程调用
- JDK/JSON 两种序列化实现
- SPI 扩展机制
- 随机、轮询、一致性哈希负载均衡
- 快速失败、重试两种集群容错策略
- ZooKeeper 注册中心扩展点
- 示例 Provider/Consumer 启动类

## 项目结构

```text
custom-rpc-framework
├── pom.xml
├── rpc-core
│   ├── pom.xml
│   ├── src/main/java/com/example/rpc
│   ├── src/main/resources/META-INF/rpc
│   └── src/test/java/com/example/rpc
└── rpc-example
    ├── pom.xml
    └── src/main/java/com/example/rpc/example
```

## 模块说明

### 1. `rpc-core`

框架核心模块，负责实现 RPC 的主要能力。

包含内容如下：

- `cluster`
  - 集群容错层
  - 包含 `ClusterInvoker`、`FailFastClusterInvoker`、`RetryClusterInvoker`
  - 用于在真正发起请求前套上一层快速失败或重试逻辑
- `codec`
  - 协议编解码层
  - 包含 `RpcMessageEncoder`、`RpcMessageDecoder`
  - 负责将 Java 对象与自定义二进制报文互相转换
- `common`
  - 公共模型
  - 当前包含 `ServiceMetadata`
  - 用于描述服务注册时的元信息
- `config`
  - 客户端/服务端配置对象
  - 包含 `RpcClientProperties`、`RpcServerProperties`
- `constant`
  - 框架常量
  - 包含协议魔数、消息类型、默认配置项等
- `discovery`
  - 服务发现层
  - 当前包含 `ServiceDiscovery`
  - 封装了注册中心查询逻辑
- `exception`
  - 统一异常定义
  - 当前包含 `RpcException`
- `loadbalance`
  - 负载均衡层
  - 包含随机、轮询、一致性哈希实现及工厂类
- `protocol`
  - RPC 协议模型
  - 包含 `RpcMessage`、`RpcRequest`、`RpcResponse`
- `proxy`
  - 动态代理层
  - 当前包含 `RpcClientProxy`
  - 将本地接口调用转换为远程 RPC 请求
- `registry`
  - 注册中心层
  - 包含 `Registry`、`ZkRegistry`、`RegistryFactory`
  - 当前预留了 ZooKeeper 注册/发现能力
- `serialize`
  - 序列化层
  - 包含 `Serializer`、`JdkSerializer`、`JsonSerializer`
- `server`
  - 服务端核心处理逻辑
  - 包含服务注册、本地服务容器、请求处理器、Netty 服务端处理器
- `spi`
  - 自定义 SPI 机制
  - 通过 `META-INF/rpc` 配置实现可插拔扩展
- `transport`
  - 网络传输层
  - 包含客户端、连接缓存、未完成请求管理、客户端处理器

### 2. `rpc-example`

示例模块，负责演示如何使用 `rpc-core`。

包含内容如下：

- `api`
  - 示例服务接口 `HelloService`
- `provider`
  - 示例服务实现 `HelloServiceImpl`
  - 服务端启动类 `ProviderApplication`
- `consumer`
  - 客户端启动类 `ConsumerApplication`
  - 演示通过代理对象发起远程调用

## SPI 配置说明

SPI 配置文件位于：

```text
rpc-core/src/main/resources/META-INF/rpc
```

当前已提供如下扩展映射：

- `com.example.rpc.serialize.Serializer`
  - `jdk`
  - `json`
- `com.example.rpc.loadbalance.LoadBalance`
  - `random`
  - `roundRobin`
  - `consistentHash`
- `com.example.rpc.cluster.ClusterInvoker`
  - `failFast`
  - `retry`
- `com.example.rpc.registry.Registry`
  - `zk`

后续如果需要新增扩展实现，只需要：

1. 实现对应接口
2. 在 `META-INF/rpc` 下增加配置映射
3. 通过工厂类按名称获取实现

## RPC 调用流程

当前示例的主调用链如下：

1. Consumer 启动后创建 `RpcClientProxy`
2. 业务代码调用代理接口方法
3. 代理层组装 `RpcRequest`
4. 负载均衡层选择目标地址
5. `RpcClient` 通过 Netty 发送 `RpcMessage`
6. 服务端解码后由 `RpcRequestHandler` 反射执行目标方法
7. 服务端返回 `RpcResponse`
8. 客户端通过 `UnprocessedRequests` 完成对应的 Future
9. 代理层返回远程调用结果

## 快速开始

### 环境要求

- JDK 8
- Maven 3.9+

### 启动 Provider

```bash
mvn -pl rpc-example -am exec:java -Dexec.mainClass=com.example.rpc.example.provider.ProviderApplication
```

### 启动 Consumer

```bash
mvn -pl rpc-example -am exec:java -Dexec.mainClass=com.example.rpc.example.consumer.ConsumerApplication
```

默认示例使用直连地址 `127.0.0.1:8088` 跑通第一阶段核心链路，因此不要求先启动 ZooKeeper。

## 配置说明

### 客户端可配置项

位置：

- `com.example.rpc.config.RpcClientProperties`

主要配置项：

- `serializer`
  - 序列化方式，默认 `jdk`
- `loadBalance`
  - 负载均衡算法，默认 `random`
- `cluster`
  - 集群容错策略，默认 `failFast`
- `registryAddress`
  - 注册中心地址，默认 `127.0.0.1:2181`
- `directServerAddress`
  - 直连服务端地址
- `timeoutMillis`
  - 请求超时时间

### 服务端可配置项

位置：

- `com.example.rpc.config.RpcServerProperties`

主要配置项：

- `host`
  - 服务监听地址
- `port`
  - 服务监听端口
- `serializer`
  - 服务端序列化方式
- `registryAddress`
  - 注册中心地址
- `registerToRegistry`
  - 是否注册到注册中心

## 测试说明

执行全部测试：

```bash
mvn test
```

当前测试覆盖内容：

- JDK 序列化正确性
- 轮询负载均衡顺序
- SPI 加载能力
- 未完成请求管理器的响应匹配能力

## 手动验证清单

- Provider 启动后，Consumer 能打印远程调用结果
- 修改 `RpcClientProperties.directServerAddress` 后，Consumer 会连接新的服务端地址
- 切换 `serializer`、`loadBalance` 或 `cluster` 后，框架仍能正常初始化
- 服务端未启动时，客户端会抛出清晰的连接异常
- 将来接入 ZooKeeper 后，可以把直连模式切换为注册中心发现模式

## 当前实现边界

当前版本更偏向“教学型最小可行框架”，已经跑通核心链路，但还有一些能力属于预留扩展点：

- ZooKeeper 已有实现类，但示例默认未自动注册
- 心跳机制已预留基础处理，但未做完整断线重连治理
- 当前客户端调用以同步阻塞等待结果为主
- 尚未加入注解式服务暴露、异步调用、连接池细化管理等增强能力

## 后续可扩展方向

- 接入完整的服务注册与发现流程
- 增加更多序列化实现，例如 Kryo、Hessian、Protobuf
- 补充故障转移、最少活跃数等策略
- 增加注解驱动的服务暴露和代理注入
- 补充更完整的集成测试和压测脚本
