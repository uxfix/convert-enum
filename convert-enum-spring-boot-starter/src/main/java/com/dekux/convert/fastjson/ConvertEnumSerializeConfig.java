package com.dekux.convert.fastjson;

import com.alibaba.fastjson.serializer.EnumSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.dekux.core.*;
import com.dekux.util.CommonUtil;

/**
 * 只能配合 fastjson 1.2.x 版本使用
 *
 * @author yuan
 * @since 1.0
 */
public class ConvertEnumSerializeConfig extends SerializeConfig {
    private final ConvertEnumContainer container;
    private final ConvertConfig convertConfig;

    public ConvertEnumSerializeConfig(ConvertEnumContainer container) {
        this.container = container;
        convertConfig = container.getConfig(ConfigTypeEnum.FASTJSON);
    }

    @Override
    protected ObjectSerializer getEnumSerializer() {
        return (serializer, object, fieldName, fieldType, features) -> {
            if (object instanceof ConvertEnum && container.containsEnum((Class<?>) fieldType)) {
                Object result = CommonUtil.policyWriter((ConvertEnum) object,
                        convertConfig.getOutType());
                serializer.write(result);
            } else {
                EnumSerializer.instance.write(serializer, object, fieldName, fieldType, features);
            }
        };
    }
}
