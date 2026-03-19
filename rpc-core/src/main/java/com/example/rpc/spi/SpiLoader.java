package com.example.rpc.spi;

import com.example.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI 加载器。
 * 负责扫描 META-INF/rpc 下的配置文件，并缓存实现类和实例。
 */
public final class SpiLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiLoader.class);
    private static final String SPI_DIRECTORY = "META-INF/rpc/";
    private static final Map<Class<?>, Map<String, Object>> INSTANCE_CACHE = new ConcurrentHashMap<Class<?>, Map<String, Object>>();
    private static final Map<Class<?>, Map<String, Class<?>>> CLASS_CACHE = new ConcurrentHashMap<Class<?>, Map<String, Class<?>>>();

    private SpiLoader() {
    }

    /**
     * 加载指定接口的全部 SPI 实现。
     *
     * @param type SPI 接口类型
     * @param <T> SPI 泛型
     */
    public static <T> void load(Class<T> type) {
        if (!type.isInterface()) {
            throw new RpcException(RpcException.ExceptionCode.CONFIG_EXCEPTION, "SPI type must be an interface: " + type.getName());
        }
        if (CLASS_CACHE.containsKey(type)) {
            return;
        }
        Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();
        String fileName = SPI_DIRECTORY + type.getName();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RpcException(RpcException.ExceptionCode.CONFIG_EXCEPTION, "SPI config not found: " + fileName);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                        continue;
                    }
                    String[] parts = trimmed.split("=");
                    if (parts.length != 2) {
                        throw new RpcException(RpcException.ExceptionCode.CONFIG_EXCEPTION, "Illegal SPI config line: " + trimmed);
                    }
                    String name = parts[0].trim();
                    Class<?> implClass = Class.forName(parts[1].trim());
                    classes.put(name, implClass);
                }
            }
        } catch (IOException e) {
            throw new RpcException(RpcException.ExceptionCode.CONFIG_EXCEPTION, "Failed to load SPI config for " + type.getName(), e);
        } catch (ClassNotFoundException e) {
            throw new RpcException(RpcException.ExceptionCode.CONFIG_EXCEPTION, "SPI implementation not found for " + type.getName(), e);
        }
        CLASS_CACHE.put(type, classes);
        INSTANCE_CACHE.put(type, new ConcurrentHashMap<String, Object>());
        LOGGER.info("Loaded {} SPI implementations for {}", classes.size(), type.getName());
    }

    /**
     * 获取指定名称的 SPI 实例。
     *
     * @param type SPI 接口类型
     * @param name 实现名称
     * @param <T> SPI 泛型
     * @return SPI 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> type, String name) {
        Objects.requireNonNull(type, "type");
        if (!CLASS_CACHE.containsKey(type)) {
            load(type);
        }
        String resolvedName = name;
        if (resolvedName == null || resolvedName.trim().isEmpty()) {
            Spi spi = type.getAnnotation(Spi.class);
            resolvedName = spi == null ? null : spi.value();
        }
        if (resolvedName == null || resolvedName.trim().isEmpty()) {
            throw new RpcException(RpcException.ExceptionCode.CONFIG_EXCEPTION, "No SPI name specified for " + type.getName());
        }
        Map<String, Object> instanceMap = INSTANCE_CACHE.get(type);
        Object cached = instanceMap.get(resolvedName);
        if (cached != null) {
            return (T) cached;
        }
        Class<?> implClass = CLASS_CACHE.get(type).get(resolvedName);
        if (implClass == null) {
            throw new RpcException(RpcException.ExceptionCode.CONFIG_EXCEPTION,
                    "No SPI implementation named " + resolvedName + " for " + type.getName());
        }
        try {
            T instance = (T) implClass.newInstance();
            instanceMap.put(resolvedName, instance);
            return instance;
        } catch (InstantiationException e) {
            throw new RpcException(RpcException.ExceptionCode.CONFIG_EXCEPTION, "Failed to instantiate SPI " + implClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new RpcException(RpcException.ExceptionCode.CONFIG_EXCEPTION, "Cannot access SPI " + implClass.getName(), e);
        }
    }

    /**
     * 获取已加载的 SPI 实现类映射。
     *
     * @param type SPI 接口类型
     * @param <T> SPI 泛型
     * @return 名称到实现类的映射
     */
    public static <T> Map<String, Class<?>> getLoadedClasses(Class<T> type) {
        if (!CLASS_CACHE.containsKey(type)) {
            load(type);
        }
        return Collections.unmodifiableMap(CLASS_CACHE.get(type));
    }
}
