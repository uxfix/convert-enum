package com.dekux.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 解析 {@link EnableConvertEnum @EnableConvertEnum} 注解获取包扫描路径
 * 将 {@link DefaultConvertEnumScan @DefaultConvertEnumScan} 注册到 Spring 容器
 *
 * @author yuan
 * @since 1.0
 */
public class EnableConvertEnumScanRegistrar implements ImportBeanDefinitionRegistrar {
    private final Set<String> basePackages = new LinkedHashSet<>();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {
        AnnotationAttributes enumScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(EnableConvertEnum.class.getName()));
        if (enumScanAttrs != null) {
            String[] basePackagesArray = enumScanAttrs.getStringArray("basePackages");
            basePackages.addAll(Arrays.stream(basePackagesArray).filter(StringUtils::hasText).collect(Collectors.toSet()));
            if (basePackages.isEmpty()) {
                basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
            }
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DefaultConvertEnumScan.class);
            builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
            registry.registerBeanDefinition(generateBaseBeanName(importingClassMetadata), builder.getBeanDefinition());
        }
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata) {
        return importingClassMetadata.getClassName() + "#" + EnableConvertEnumScanRegistrar.class.getSimpleName();
    }
}
