package com.dekux.core;

/**
 * 当你的枚举类需要被 {@link ConvertEnumContainer @ConvertEnumContainer} 容器管理时应该实现该接口,
 * 你应该保证在同一个枚举类型里面,它的 code 和 desc 值都是唯一的
 *
 * @author yuan
 * @since 1.0
 */
public interface ConvertEnum {

    /**
     * 返回枚举类的类型码值
     *
     * @return code
     */
    int getCode();

    /**
     * 返回枚举类的描述信息
     *
     * @return desc
     */
    String getDesc();
}
