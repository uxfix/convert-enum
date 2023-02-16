package com.dekux.convert.fastjson;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.dekux.core.ConfigTypeEnum;
import com.dekux.core.ConvertConfig;
import com.dekux.core.ConvertEnum;
import com.dekux.core.ConvertEnumContainer;
import com.dekux.util.CommonUtil;

import java.lang.reflect.Type;

/**
 * 只能配合 fastjson 1.2.x 版本使用
 *
 * @author yuan
 * @since 1.0
 */
public class ConvertEnumParserConfig extends ParserConfig {
    private final ConvertEnumContainer container;
    private final ConvertConfig convertConfig;

    public ConvertEnumParserConfig(ConvertEnumContainer container) {
        this.container = container;
        convertConfig = container.getConfig(ConfigTypeEnum.FASTJSON);
    }

    @Override
    protected ObjectDeserializer getEnumDeserializer(Class<?> clazz) {
        if (CommonUtil.hasConvertEnumInterface(clazz.getInterfaces(), ConvertEnum.class)
                && container.containsEnum(clazz)) {
            return new ObjectDeserializer() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
                    try {
                        Object value;
                        final JSONLexer lexer = parser.lexer;
                        final int token = lexer.token();
                        if (token == JSONToken.LITERAL_INT) {
                            value = lexer.intValue();
                        } else if (token == JSONToken.LITERAL_STRING) {
                            value = lexer.stringVal();
                            lexer.nextToken(JSONToken.COMMA);
                        } else if (token == JSONToken.NULL) {
                            lexer.nextToken(JSONToken.COMMA);
                            return null;
                        } else {
                            value = parser.parse();
                        }
                        Object result = container.get(value, (Class<?>) type, convertConfig.getInType());
                        if (result == null) {
                            throw new JSONException("parse enum " + type.getTypeName() + " error, value : " + value);
                        }
                        return (T) result;
                    } catch (JSONException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new JSONException(e.getMessage(), e);
                    }
                }

                @Override
                public int getFastMatchToken() {
                    return JSONToken.LITERAL_INT;
                }
            };
        } else {
            return super.getEnumDeserializer(clazz);
        }
    }
}
