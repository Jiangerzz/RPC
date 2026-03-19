package com.example.rpc.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * SPI 标记注解。
 * 用于标识某个接口支持 SPI 扩展，并声明默认实现名称。
 */
public @interface Spi {
    /**
     * 返回默认实现名称。
     *
     * @return 默认实现名
     */
    String value() default "";
}
