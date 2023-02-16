package com.dekux.util;

import com.dekux.core.ConvertEnum;
import com.dekux.core.IOTypeEnum;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 一些公共通用方法
 *
 * @author yuan
 * @since 1.0
 */
public abstract class CommonUtil {

    public static boolean hasConvertEnumInterface(Class<?>[] interfaces, Class<?> classes) {
        for (Class<?> interfaceClass : interfaces) {
            if (classes.equals(interfaceClass)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasConvertEnumInterface(String[] interfaceNames, String className) {
        for (String interfaceName : interfaceNames) {
            if (className.equals(interfaceName)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> enumValues(Class<?> enumClass) {
        return Arrays.stream(getEnumArray(enumClass)).map(Enum::name).collect(Collectors.toList());
    }

    public static Enum<?>[] getEnumArray(Class<?> enumClass) {
        if (enumClass.isEnum()) {
            try {
                Method values = enumClass.getDeclaredMethod("values");
                return (Enum<?>[]) values.invoke(null);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new Enum[]{};
    }

    public static Object policyWriter(ConvertEnum convertEnum, IOTypeEnum typeEnum) {
        Enum<?> e = ((Enum<?>) convertEnum);
        switch (typeEnum) {
            case CODE:
                return convertEnum.getCode();
            case NAME:
                return e.name();
            case DESC:
                return convertEnum.getDesc();
            case ORDINAL:
                return e.ordinal();
            default:
                return null;
        }
    }
}
