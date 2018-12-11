package me.mingshan.util;

import java.util.Objects;

/**
 * Provides common methods to operate class.
 *
 * @author mingshan
 */
public class ClassUtil {

    private ClassUtil() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }

    /**
     * Gets class loader.
     *
     * @param clazz the current class.
     * @return the class loader
     */
    public static ClassLoader getClassLoader(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        ClassLoader c1 = Thread.currentThread().getContextClassLoader();

        if (c1 == null) {
            c1 = clazz.getClassLoader();
            if (c1 == null) {
                c1 = ClassLoader.getSystemClassLoader();
            }
        }

        return c1;
    }

    /**
     * Gets class loader.
     *
     * @return the class loader.
     */
    public static ClassLoader getClassLoader() {
        return getClassLoader(ClassUtil.class);
    }
}
