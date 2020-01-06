package me.mingshan.util.propertys;

/**
 * Interface that providers the common methods to get {@link Property}.
 *
 * @author mingshan
 */
public interface CustomProperties {

    /**
     * Gets a {@code String} property that maybe not exist.
     *
     * @param name the key of property
     * @param fallback the default value, maybe is {@code null}
     * @return never {@code null}
     */
    Property<String> getString(String name, String fallback);

    /**
     * Gets a {@code Integer} property that maybe not exist.
     *
     * @param name the key of property
     * @param fallback the default value, maybe is {@code null}
     * @return never {@code null}
     */
    Property<Integer> getInteger(String name, Integer fallback);

    /**
     * Gets a {@code Boolean} property that maybe not exist.
     *
     * @param name the key of property
     * @param fallback the default value, maybe is {@code null}
     * @return never {@code null}
     */
    Property<Boolean> getBoolean(String name, Boolean fallback);

    /**
     * Gets a {@code Long} property that maybe not exist.
     *
     * @param name the key of property
     * @param fallback the default value, maybe is {@code null}
     * @return never {@code null}
     */
    Property<Long> getLong(String name, Long fallback);
}