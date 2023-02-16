package com.dekux.spring;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.dekux.convert.fastjson.ConvertEnumParserConfig;
import com.dekux.convert.fastjson.ConvertEnumSerializeConfig;
import com.dekux.convert.jackson.BeanDeserializerFactoryConvertEnum;
import com.dekux.convert.jackson.BeanSerializerFactoryConvertEnum;
import com.dekux.convert.mybatis.ConvertEnumTypeHandler;
import com.dekux.core.ConvertEnumContainer;
import com.dekux.core.ConvertEnumScan;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 自动配置类
 *
 * @author yuan
 * @since 1.0
 */
@Configuration
public class ConvertEnumAutoConfiguration {

    @Bean
    @ConditionalOnBean(ConvertEnumScan.class)
    public ConvertEnumContainer convertEnumContainer(ConvertEnumScan scan,
                                                     ObjectProvider<List<ConvertEnumContainerCustomizer>> containerCustomizerProvider) {
        DefaultConvertEnumContainer container = new DefaultConvertEnumContainer(scan);
        List<ConvertEnumContainerCustomizer> containerCustomizers = containerCustomizerProvider.getIfAvailable();
        if (!CollectionUtils.isEmpty(containerCustomizers)) {
            for (ConvertEnumContainerCustomizer customizer : containerCustomizers) {
                customizer.customize(container);
            }
        }
        return container;
    }


    @ConditionalOnClass({SqlSessionFactory.class})
    static class MybatisAutoConfiguration{
        @Bean
        @ConditionalOnBean(ConvertEnumScan.class)
        public ConfigurationCustomizer configurationCustomizer(ConvertEnumContainer container) {
            return configuration -> {
                Set<Class<?>> enumClassSet = container.getEnumClassAll();
                if (enumClassSet != null && enumClassSet.size() > 0) {
                    TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                    for (Class<?> enumClass : enumClassSet) {
                        ConvertEnumTypeHandler convertEnumTypeHandler = new ConvertEnumTypeHandler(container, enumClass);
                        typeHandlerRegistry.register(enumClass, null, convertEnumTypeHandler);
                    }
                }
            };
        }
    }

    @ConditionalOnClass(ObjectMapper.class)
    static class JacksonAutoConfiguration {
        @Bean
        @ConditionalOnBean(ConvertEnumScan.class)
        public ObjectMapper objectMapper(ConvertEnumContainer container) {
            BeanDeserializerFactoryConvertEnum beanDeserializerFactoryConvertEnum =
                    new BeanDeserializerFactoryConvertEnum(container);
            BeanSerializerFactoryConvertEnum beanSerializerFactoryConvertEnum =
                    new BeanSerializerFactoryConvertEnum(container);
            ObjectMapper objectMapper = new ObjectMapper(null, null,
                    new DefaultDeserializationContext.Impl(beanDeserializerFactoryConvertEnum));
            objectMapper.setSerializerFactory(beanSerializerFactoryConvertEnum);
            return objectMapper;
        }
    }

    @ConditionalOnClass({FastJsonHttpMessageConverter4.class})
    static class FastJsonAutoConfiguration {
        @Bean
        @ConditionalOnBean(ConvertEnumScan.class)
        public ConvertEnumSerializeConfig convertEnumSerializeConfig(ConvertEnumContainer container) {
            return new ConvertEnumSerializeConfig(container);
        }

        @Bean
        @ConditionalOnBean(ConvertEnumScan.class)
        public ConvertEnumParserConfig convertEnumParserConfig(ConvertEnumContainer container) {
            return new ConvertEnumParserConfig(container);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnBean(ConvertEnumScan.class)
        public FastJsonHttpMessageConverter fastJsonHttpMessageConverter(ConvertEnumSerializeConfig serializeConfig,
                                                                         ConvertEnumParserConfig parserConfig) {
            FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
            FastJsonConfig fastJsonConfig = new FastJsonConfig();
            fastJsonConfig.setSerializeConfig(serializeConfig);
            fastJsonConfig.setParserConfig(parserConfig);
            fastConverter.setFastJsonConfig(fastJsonConfig);
            // 添加支持的 MediaTypes;不添加时默认为*/*
            // 也就是默认支持全部,在某些 Spring 版本会报错
            List<MediaType> fastMediaTypes = new ArrayList<>();
            fastMediaTypes.add(MediaType.APPLICATION_JSON);
            fastConverter.setSupportedMediaTypes(fastMediaTypes);
            return fastConverter;
        }
    }

}
