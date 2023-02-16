package com.dekux.convert.jackson;

import com.dekux.core.ConfigTypeEnum;
import com.dekux.core.ConvertConfig;
import com.dekux.core.ConvertEnum;
import com.dekux.core.ConvertEnumContainer;
import com.dekux.util.CommonUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;

import java.io.IOException;

/**
 * 继承自 {@link BeanDeserializerFactory @BeanDeserializerFactory} 重写覆盖了它的创建枚举序列化对象工厂方法,
 * 以此来完成自定义枚举反序列化逻辑
 *
 * @author yuan
 * @since 1.0
 */
public class BeanDeserializerFactoryConvertEnum extends BeanDeserializerFactory {
    private final ConvertEnumContainer container;
    private final ConvertConfig convertConfig;

    public BeanDeserializerFactoryConvertEnum(ConvertEnumContainer container) {
        super(new DeserializerFactoryConfig());
        this.container = container;
        convertConfig = container.getConfig(ConfigTypeEnum.JACKSON);
    }

    @Override
    public JsonDeserializer<?> createEnumDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        Class<?> enumClass = type.getRawClass();
        if (CommonUtil.hasConvertEnumInterface(enumClass.getInterfaces(), ConvertEnum.class)
                && container.containsEnum(enumClass)) {
            return new JsonDeserializer<Object>() {
                @Override
                public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    String text = p.getText().trim();
                    Object result = container.get(text, enumClass, convertConfig.getInType());
                    if (result == null) {
                        throw ctxt.weirdStringException(p.getText(), enumClass, failMsg(enumClass));
                    }
                    return result;
                }
            };
        } else {
            // 没有实现 ConvertEnum 接口或者未被容器管理那么不处理使用默认的
            return super.createEnumDeserializer(ctxt, type, beanDesc);
        }
    }

    private String failMsg(Class<?> enumClass) {
        return String.format("not one of the values accepted for Enum class: %s",
                CommonUtil.enumValues(enumClass));
    }
}
