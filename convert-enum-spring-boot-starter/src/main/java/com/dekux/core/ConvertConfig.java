package com.dekux.core;

/**
 * 该配置接口定义使用何种输入类型转换成枚举对象
 * 以及枚举的转换输出数据类型是什么
 *
 * @author yuan
 * @see IOTypeEnum
 * @since 1.0
 */
public interface ConvertConfig {

    /**
     * 定义使用那种输入类型来完成对枚举的映射转换
     *
     * @return 输入类型
     */
    IOTypeEnum getInType();

    /**
     * 定义使用那种输出类型来完成对枚举的映射输出
     *
     * @return 输出类型
     */
    IOTypeEnum getOutType();
}
