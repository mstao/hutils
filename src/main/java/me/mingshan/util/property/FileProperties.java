package me.mingshan.util.property;

import me.mingshan.util.PropertyUtil;
import me.mingshan.util.StringUtil;
import me.mingshan.util.cache.Cache;
import me.mingshan.util.cache.CachePrefix;
import me.mingshan.util.cache.caffeine.CaffeineCache;

import java.util.Properties;

/**
 * Provides the way to get configuration via property file.
 *
 * @author mingshan
 */
public class FileProperties implements CustomProperties {
    /**
     * The name of property file.
     */
    public static final String PROPERTY_FILE_NAME = "magpie.properties";

    /**
     * No Public
     */
    private FileProperties() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }

    /**
     * Inner class for lazy load.
     */
    private static final class FilePropertiesHolder {
        private static final FileProperties INSTANCE = new FileProperties();
    }

    /**
     * Returns the instance of {@link FileProperties}.
     *
     * @return the instance of {@link FileProperties}
     */
    public static FileProperties getInstance() {
        return FilePropertiesHolder.INSTANCE;
    }

    @Override
    public Property<String> getString(String name, String fallback) {
        Cache caffeineCache = CaffeineCache.getInstance();
        Object cachedValue = caffeineCache.get(CachePrefix.STRING.getValue() + name);
        String value = null;
        if (cachedValue == null) {
            Properties properties = PropertyUtil.loadProperties(PROPERTY_FILE_NAME);
            Object resource = properties.get(name);
            if (resource != null) {
                String tempValue = String.valueOf(resource);
                if (!StringUtil.isEmpty(tempValue)) {
                    value = tempValue;
                    caffeineCache.put(CachePrefix.STRING.getValue() + name, value);
                }
            }
        } else {
            value = String.valueOf(cachedValue);
        }

        final String endValue = value;
        return new Property<String>() {

            @Override
            public String get() {
                return endValue;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    @Override
    public Property<Integer> getInteger(String name, Integer fallback) {
        Cache caffeineCache = CaffeineCache.getInstance();
        Object cachedValue = caffeineCache.get(CachePrefix.INTEGER.getValue() + name);
        Integer value;
        if (cachedValue == null) {
            Properties properties = PropertyUtil.loadProperties(PROPERTY_FILE_NAME);
            value = Integer.valueOf(properties.get(name).toString());
            caffeineCache.put(CachePrefix.INTEGER.getValue() + name, value);
        } else {
            value = Integer.valueOf(cachedValue.toString());
        }

        return new Property<Integer>() {

            @Override
            public Integer get() {
                return value;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    @Override
    public Property<Boolean> getBoolean(String name, Boolean fallback) {
        Cache caffeineCache = CaffeineCache.getInstance();
        Object cachedValue = caffeineCache.get(CachePrefix.BOOLEAN.getValue() + name);
        Boolean value;
        if (cachedValue == null) {
            Properties properties = PropertyUtil.loadProperties(PROPERTY_FILE_NAME);
            value = Boolean.valueOf(properties.get(name).toString());
            caffeineCache.put(CachePrefix.BOOLEAN.getValue() + name, value);
        } else {
            value = Boolean.valueOf(cachedValue.toString());
        }

        return new Property<Boolean>() {

            @Override
            public Boolean get() {
                return value;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    @Override
    public Property<Long> getLong(String name, Long fallback) {
        Cache caffeineCache = CaffeineCache.getInstance();
        Object cachedValue = caffeineCache.get(CachePrefix.LONG.getValue() + name);
        Long value;
        if (cachedValue == null) {
            Properties properties = PropertyUtil.loadProperties(PROPERTY_FILE_NAME);
            value = Long.valueOf(properties.get(name).toString());
            caffeineCache.put(CachePrefix.LONG.getValue() + name, value);
        } else {
            value = Long.valueOf(cachedValue.toString());
        }

        return new Property<Long>() {

            @Override
            public Long get() {
                return value;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}