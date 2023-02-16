package com.dekux.spring;

import com.dekux.core.ConvertConfig;
import com.dekux.core.ConvertEnumContainer;

/**
 * 在枚举容器 {@link ConvertEnumContainer} 创建完成后的接口回调,可以对容器进行自定义设置,
 * 比方说往容器内设置你自定义的 {@link ConvertConfig}, 你只需要将该接口的实现类注册成为 Spring 容器的 Bean
 *
 * @author yuan
 * @since 1.0
 */
@FunctionalInterface
public interface ConvertEnumContainerCustomizer {

    void customize(ConvertEnumContainer container);
}
