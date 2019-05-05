package me.mingshan.util.cache;

/**
 * The prefix enum for local cache.
 *
 * @author mingshan
 */
public enum CachePrefix {

    /**
     * The prefix of `String`
     */
    STRING("String#"),

    /**
     * The prefix of `Integer`
     */
    INTEGER("Integer#"),

    /**
     * The prefix of `Boolean`
     */
    BOOLEAN("Boolean#"),

    /**
     * The prefix of `Long`
     */
    LONG("Long#");

    private String value;

    CachePrefix(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}