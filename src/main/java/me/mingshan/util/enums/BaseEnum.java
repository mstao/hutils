package me.mingshan.util.enums;

/**
 * 基础枚举
 *
 */
public interface BaseEnum {

    int getCode();

    String getDesc();

    static <T extends Enum<T>> T codeOf(Class<T> enumClass, int code) {
        if (enumClass.isAssignableFrom(BaseEnum.class)) {
            throw new IllegalArgumentException("Illegal explicit Enum type");
        }
        T[] enums = enumClass.getEnumConstants();
        for (T t : enums) {
            BaseEnum explicitEnum = (BaseEnum) t;
            if (code == explicitEnum.getCode()) {
                return t;
            }
        }
        throw new IllegalArgumentException("Cannot parse int: " + code + " to " + enumClass.getName());
    }

    static <T extends Enum<T>> T descOf(Class<T> enumClass, String desc) {
        if (desc == null) {
            throw new IllegalArgumentException("Explicit Enum desc should not be null");
        }
        if (enumClass.isAssignableFrom(BaseEnum.class)) {
            throw new IllegalArgumentException("Illegal explicit Enum type");
        }
        T[] enums = enumClass.getEnumConstants();
        for (T t : enums) {
            BaseEnum explicitEnum = (BaseEnum) t;
            if (desc.equalsIgnoreCase(explicitEnum.getDesc())) {
                return t;
            }
        }
        throw new IllegalArgumentException("Cannot parse string: " + desc + " to " + enumClass.getName());
    }
}
