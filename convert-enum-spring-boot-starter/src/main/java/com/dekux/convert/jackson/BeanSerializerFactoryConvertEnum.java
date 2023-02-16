package com.dekux.convert.jackson;

import com.dekux.core.ConfigTypeEnum;
import com.dekux.core.ConvertConfig;
import com.dekux.core.ConvertEnum;
import com.dekux.core.ConvertEnumContainer;
import com.dekux.util.CommonUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;

import java.io.IOException;

/**
 * 继承自 {@link BeanSerializerFactory @BeanSerializerFactory} 重写覆盖了它的创建枚举序列化对象工厂方法,
 * 以此来完成自定义枚举序列化逻辑, 通常来说这是优先级最低的序列化逻辑,如果你使用注解指定了自定义序列化逻辑,
 * 那么该序列化逻辑则不会生效,反序列化同理
 *
 * @author yuan
 * @since 1.0
 */
public class BeanSerializerFactoryConvertEnum extends BeanSerializerFactory {
    private final ConvertConfig convertConfig;
    private final ConvertEnumContainer container;

    public BeanSerializerFactoryConvertEnum(ConvertEnumContainer container) {
        super(new SerializerFactoryConfig());
        this.container = container;
        convertConfig = container.getConfig(ConfigTypeEnum.JACKSON);
    }

    @Override
    protected JsonSerializer<?> buildEnumSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        Class<?> rawClass = type.getRawClass();
        if (container.containsEnum(rawClass)) {
            return new JsonSerializer<Object>() {
                @Override
                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    if (value instanceof ConvertEnum) {
                        ConvertEnum convertEnum = (ConvertEnum) value;
                        gen.writeObject(CommonUtil.policyWriter(convertEnum, convertConfig.getOutType()));
                    }
                }
            };
        } else {
            return super.buildEnumSerializer(config, type, beanDesc);
        }
    }
}
