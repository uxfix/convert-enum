package com.dekux.core;

import java.util.Set;

/**
 * 管理所有实现了 {@link ConvertEnum @ConvertEnum} 接口的枚举类,
 * 以及枚举转换器所有的转换配置对象 {@link ConvertConfig @ConvertConfig}
 *
 * @author yuan
 * @since 1.0
 */
public interface ConvertEnumContainer {

    /**
     * 检查该容器是否存在指定类型枚举
     *
     * @param enumType 枚举类型
     * @return 是否存在
     */
    boolean containsEnum(Class<?> enumType);

    /**
     * 获取容器内所有的的枚举 class
     *
     * @return 枚举 class
     */
    Set<Class<?>> getEnumClassAll();

    /**
     * 根据参数条件获取对应的枚举对象
     *
     * @param key      key 取决于你的 {@link IOTypeEnum @IOTypeEnum} 类型
     *                 假设 IOTypeEnum 类型为 CODE, 那么你的 Key 应该为 ConvertEnum#getCode()
     * @param enumType 枚举类型
     * @param ioType   输入类型
     * @param <T>      枚举类型
     * @return 枚举
     * @see ConvertEnum#getCode()
     */
    <T> T get(Object key, Class<T> enumType, IOTypeEnum ioType);

    /**
     * 添加一个枚举到容器内
     *
     * @param enumClass 枚举 class
     */
    void register(Class<? extends ConvertEnum> enumClass);

    /**
     * 根据参数转换器类型获取枚举转换映射时的配置类
     *
     * @param configType 转换器类型
     * @return 配置对象
     */
    ConvertConfig getConfig(ConfigTypeEnum configType);

    /**
     * 为指定的转换器设置对应的转换配置
     *
     * @param configType    转换器类型
     * @param convertConfig 转换配置对象
     */
    void setConfig(ConfigTypeEnum configType, ConvertConfig convertConfig);
}
