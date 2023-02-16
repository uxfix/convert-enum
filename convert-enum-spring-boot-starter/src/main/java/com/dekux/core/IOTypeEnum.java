package com.dekux.core;

/**
 * 该枚举类定义了四种映射类型,允许你自定义你的输入或输入如何转换成你的枚举
 * @author yuan
 * @see com.dekux.util.CommonUtil#policyWriter(ConvertEnum, IOTypeEnum)
 * @since 1.0
 */
public enum IOTypeEnum {

    /**
     * @see ConvertEnum#getCode()
     */
    CODE(0),

    /**
     * @see ConvertEnum#getDesc()
     */
    DESC(1),

    /**
     * @see Enum#name()
     */
    NAME(2),

    /**
     * @see Enum#ordinal()
     */
    ORDINAL(3);

    private final int code;

    IOTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
