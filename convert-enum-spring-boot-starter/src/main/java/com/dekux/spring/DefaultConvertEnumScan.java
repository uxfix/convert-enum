package com.dekux.spring;

import com.dekux.core.ConvertEnum;
import com.dekux.core.ConvertEnumScan;
import com.dekux.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 基于 Spring 的默认实现枚举扫描器
 * 依赖于 {@link EnableConvertEnumScanRegistrar @EnableConvertEnumScanRegistrar} 注册到 Spring 容器
 *
 * @author yuan
 * @since 1.0
 */
public class DefaultConvertEnumScan implements ConvertEnumScan, EnvironmentAware {
    private final PathMatchingResourcePatternResolver scan = new PathMatchingResourcePatternResolver();
    private static final Logger log = LoggerFactory.getLogger(DefaultConvertEnumScan.class);
    private String basePackage;
    private final Set<String> classNameSet = new HashSet<>();
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
    private static final String ENUM_CLASS_NAME = "java.lang.Enum";
    private static final String CONVERT_ENUM_CLASS_NAME = ConvertEnum.class.getName();
    private Environment environment;
    private MetadataReaderFactory metadataReaderFactory;

    @PostConstruct
    public void init() {
        String[] basePackageArray = StringUtils.tokenizeToStringArray(this.basePackage, ",; \t\n");
        doScan(basePackageArray);
    }

    private void doScan(String[] basePackageArray) {
        Assert.notEmpty(basePackageArray, "Parameter 'basePackageArray' not Empty");
        for (String pkg : basePackageArray) {
            try {
                String packageSearchPath = CLASSPATH_ALL_URL_PREFIX +
                        resolveBasePackage(pkg) + '/' + DEFAULT_RESOURCE_PATTERN;
                Resource[] resourceArray = scan.getResources(packageSearchPath);
                for (Resource resource : resourceArray) {
                    MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);
                    ClassMetadata classMetadata = metadataReader.getClassMetadata();
                    if (ENUM_CLASS_NAME.equals(classMetadata.getSuperClassName())) {
                        String[] interfaceNames = classMetadata.getInterfaceNames();
                        if (CommonUtil.hasConvertEnumInterface(interfaceNames, CONVERT_ENUM_CLASS_NAME)) {
                            classNameSet.add(classMetadata.getClassName());
                        }
                    }
                }
            } catch (IOException e) {
                log.warn("{}: doScan error ignore", pkg, e);
            }
        }
    }

    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(getEnvironment().resolveRequiredPlaceholders(basePackage));
    }

    public final Environment getEnvironment() {
        if (this.environment == null) {
            this.environment = new StandardEnvironment();
        }
        return this.environment;
    }

    public final MetadataReaderFactory getMetadataReaderFactory() {
        if (this.metadataReaderFactory == null) {
            this.metadataReaderFactory = new CachingMetadataReaderFactory();
        }
        return this.metadataReaderFactory;
    }

    public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory) {
        this.metadataReaderFactory = metadataReaderFactory;
    }

    @Override
    public Class<?>[] getConvertEnumClasses() {
        return new Class[0];
    }

    @Override
    public String[] getConvertEnumClassName() {
        return classNameSet.toArray(new String[]{});
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
