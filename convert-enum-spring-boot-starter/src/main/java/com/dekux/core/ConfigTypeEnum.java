package com.dekux.core;

/**
 * 映射在各种框架下的枚举转换配置
 *
 * @author yuan
 * @see ConvertEnumContainer#getConfig(ConfigTypeEnum)
 * @see ConvertEnumContainer#setConfig(ConfigTypeEnum, ConvertConfig)
 * @since 1.0
 */
public enum ConfigTypeEnum {

    JACKSON(0),
    FASTJSON(1),
    MYBATIS(2),
    DATA_JPA(3);

    private final int code;

    ConfigTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
