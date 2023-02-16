package com.dekux.spring;

import com.dekux.core.ConvertConfig;
import com.dekux.core.IOTypeEnum;

/**
 * 默认的转换配置类
 *
 * @author yuan
 * @since 1.0
 */
public class DefaultConvertConfig implements ConvertConfig {

    private IOTypeEnum inType = IOTypeEnum.CODE;
    private IOTypeEnum outType = IOTypeEnum.CODE;

    public DefaultConvertConfig() {
    }

    public DefaultConvertConfig(IOTypeEnum inType, IOTypeEnum outType) {
        this.inType = inType;
        this.outType = outType;
    }

    @Override
    public IOTypeEnum getInType() {
        return inType;
    }

    @Override
    public IOTypeEnum getOutType() {
        return outType;
    }

    public void setInType(IOTypeEnum inType) {
        this.inType = inType;
    }

    public void setOutType(IOTypeEnum outType) {
        this.outType = outType;
    }
}
