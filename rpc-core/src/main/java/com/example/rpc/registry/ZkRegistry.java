package com.example.rpc.registry;

import com.example.rpc.common.ServiceMetadata;
import com.example.rpc.constant.RpcConstants;
import com.example.rpc.exception.RpcException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于 ZooKeeper 的注册中心实现。
 * 使用 Curator 简化节点创建、查询和连接管理。
 */
public class ZkRegistry implements Registry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkRegistry.class);
    private final CuratorFramework client;

    /**
     * 使用默认 ZooKeeper 地址创建注册中心实例。
     */
    public ZkRegistry() {
        this("127.0.0.1:2181");
    }

    /**
     * 使用指定 ZooKeeper 地址创建注册中心实例。
     *
     * @param address ZooKeeper 连接地址
     */
    public ZkRegistry(String address) {
        this.client = CuratorFrameworkFactory.builder()
                .connectString(address)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        this.client.start();
    }

    /**
     * 将服务实例注册到 ZooKeeper。
     *
     * @param metadata 服务元数据
     */
    public void register(ServiceMetadata metadata) {
        String servicePath = RpcConstants.REGISTRY_ROOT_PATH + "/" + metadata.getServiceInterface();
        String nodePath = servicePath + "/" + metadata.toAddress();
        try {
            if (client.checkExists().forPath(RpcConstants.REGISTRY_ROOT_PATH) == null) {
                client.create().creatingParentsIfNeeded().forPath(RpcConstants.REGISTRY_ROOT_PATH);
            }
            if (client.checkExists().forPath(servicePath) == null) {
                client.create().creatingParentsIfNeeded().forPath(servicePath);
            }
            if (client.checkExists().forPath(nodePath) != null) {
                client.delete().forPath(nodePath);
            }
            client.create().withMode(org.apache.zookeeper.CreateMode.EPHEMERAL).forPath(nodePath, metadata.toAddress().getBytes());
            LOGGER.info("Registered service {}", nodePath);
        } catch (Exception e) {
            throw new RpcException(RpcException.ExceptionCode.REGISTRY_EXCEPTION, "Failed to register service " + nodePath, e);
        }
    }

    /**
     * 从 ZooKeeper 注销服务实例。
     *
     * @param metadata 服务元数据
     */
    public void unregister(ServiceMetadata metadata) {
        String nodePath = RpcConstants.REGISTRY_ROOT_PATH + "/" + metadata.getServiceInterface() + "/" + metadata.toAddress();
        try {
            if (client.checkExists().forPath(nodePath) != null) {
                client.delete().forPath(nodePath);
            }
        } catch (Exception e) {
            throw new RpcException(RpcException.ExceptionCode.REGISTRY_EXCEPTION, "Failed to unregister service " + nodePath, e);
        }
    }

    /**
     * 查询某个服务的全部可用地址。
     *
     * @param serviceName 服务名称
     * @return 地址列表
     */
    public List<String> discover(String serviceName) {
        String servicePath = RpcConstants.REGISTRY_ROOT_PATH + "/" + serviceName;
        try {
            if (client.checkExists().forPath(servicePath) == null) {
                return new ArrayList<String>();
            }
            return client.getChildren().forPath(servicePath);
        } catch (Exception e) {
            throw new RpcException(RpcException.ExceptionCode.REGISTRY_EXCEPTION, "Failed to discover service " + serviceName, e);
        }
    }

    /**
     * 关闭 Curator 客户端并释放资源。
     */
    public void destroy() {
        client.close();
    }
}
