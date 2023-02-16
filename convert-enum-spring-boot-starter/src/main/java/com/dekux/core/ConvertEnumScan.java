package com.dekux.core;

/**
 * 扫描指定的包路径下的枚举
 * 通常 {@link ConvertEnumContainer @ConvertEnumContainer} 容器需要依赖此实现来获取枚举类
 *
 * @author yuan
 * @since 1.0
 */
public interface ConvertEnumScan {

    /**
     * 获取枚举类
     *
     * @return 枚举类
     */
    Class<?>[] getConvertEnumClasses();

    /**
     * 获取枚举类的 className
     *
     * @return 枚举类 className
     */
    String[] getConvertEnumClassName();
}
