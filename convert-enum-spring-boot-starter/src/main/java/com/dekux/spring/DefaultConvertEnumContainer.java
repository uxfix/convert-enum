package com.dekux.spring;

import com.dekux.core.*;
import com.dekux.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 管理实现了 {@link ConvertEnum @ConvertEnum} 接口的枚举类
 * 依赖于 {@link EnableConvertEnumScanRegistrar @EnableConvertEnumScanRegistrar} 完成包扫描获取枚举类
 *
 * @author yuan
 * @since 1.0
 */
public class DefaultConvertEnumContainer implements ConvertEnumContainer {
    private static final Logger log = LoggerFactory.getLogger(DefaultConvertEnumContainer.class);
    private final Set<Class<?>> convertEnumClassSet = new HashSet<>();
    private final Map<Class<?>, Map<Object, ConvertEnum>> container = new HashMap<>();
    private final ConvertConfig DEFAULT_CONVERT_CONFIG = new DefaultConvertConfig();
    private final Map<ConfigTypeEnum, ConvertConfig> convertConfigMap = new HashMap<>(4);

    public DefaultConvertEnumContainer(ConvertEnumScan scan) {
        handleClasses(scan.getConvertEnumClasses());
        handleClassName(scan.getConvertEnumClassName());
        buildContainer();
    }

    private void handleClasses(Class<?>[] classesArray) {
        if (classesArray != null && classesArray.length > 0) {
            Collections.addAll(convertEnumClassSet, classesArray);
        }
    }

    private void handleClassName(String[] classNameArray) {
        if (classNameArray != null && classNameArray.length > 0) {
            for (String className : classNameArray) {
                try {
                    Class<?> classes = Class.forName(className);
                    convertEnumClassSet.add(classes);
                } catch (ClassNotFoundException e) {
                    log.warn("handleClassName:{} error ignore", className, e);
                }
            }
        }
    }

    private void buildContainer() {
        for (Class<?> classes : convertEnumClassSet) {
            putConvertEnum(classes);
        }
    }

    private void putConvertEnum(Class<?> enumClass) {
        if (enumClass.isEnum()) {
            Class<?>[] interfaces = enumClass.getInterfaces();
            if (CommonUtil.hasConvertEnumInterface(interfaces, ConvertEnum.class)) {
                ConvertEnum[] convertEnumArray = (ConvertEnum[]) CommonUtil.getEnumArray(enumClass);
                for (ConvertEnum convertEnum : convertEnumArray) {
                    Map<Object, ConvertEnum> convertEnumMap = getConvertEnumMap(enumClass);
                    convertEnumMap.put(generateKey(convertEnum.getCode(), IOTypeEnum.CODE), convertEnum);
                    convertEnumMap.put(generateKey(convertEnum.getDesc(), IOTypeEnum.DESC), convertEnum);
                    Enum<?> e = ((Enum<?>) convertEnum);
                    convertEnumMap.put(generateKey(e.name(), IOTypeEnum.NAME), convertEnum);
                    convertEnumMap.put(generateKey(e.ordinal(), IOTypeEnum.ORDINAL), convertEnum);
                }
            }
        }
    }

    private String generateKey(Object key, IOTypeEnum ioType) {
        switch (ioType) {
            case CODE:
                return String.format("code#%s", key);
            case DESC:
                return String.format("desc#%s", key);
            case NAME:
                return String.format("name#%s", key);
            case ORDINAL:
                return String.format("ordinal#%s", key);
            default:
                return null;
        }
    }

    @Override
    public boolean containsEnum(Class<?> enumType) {
        return container.containsKey(enumType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> enumType, IOTypeEnum ioType) {
        if (containsEnum(enumType)) {
            String strKey = generateKey(key, ioType);
            return (T) getConvertEnumMap(enumType).get(strKey);
        }
        return null;
    }

    private Map<Object, ConvertEnum> getConvertEnumMap(Class<?> key) {
        return container.computeIfAbsent(key, k -> new HashMap<>());

    }

    @Override
    public void register(Class<? extends ConvertEnum> enumClass) {
        putConvertEnum(enumClass);
    }

    @Override
    public ConvertConfig getConfig(ConfigTypeEnum configType) {
        return convertConfigMap.computeIfAbsent(configType, k -> DEFAULT_CONVERT_CONFIG);
    }

    @Override
    public void setConfig(ConfigTypeEnum configType, ConvertConfig convertConfig) {
        convertConfigMap.put(configType, convertConfig);
    }

    @Override
    public Set<Class<?>> getEnumClassAll() {
        return container.keySet();
    }
}
