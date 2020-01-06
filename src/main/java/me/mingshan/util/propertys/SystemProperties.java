package me.mingshan.util.propertys;

/**
 * Provides the way to get configuration via system property.
 *
 * @author mingshan
 */
public class SystemProperties implements CustomProperties {

    /**
     * No public
     */
    private SystemProperties() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }

    /**
     * Inner class for lazy load.
     */
    private static class SystemPropertiesHolder {
        private static final SystemProperties INSTANCE = new SystemProperties();
    }

    /**
     * Returns the instance of {@link SystemProperties}.
     *
     * @return the instance of {@link SystemProperties}
     */
    public static SystemProperties getInstance() {
        return SystemPropertiesHolder.INSTANCE;
    }

    @Override
    public Property<String> getString(String name, String fallback) {
        return new Property<String>() {
            @Override
            public String get() {
                return System.getProperty(name, fallback);
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    @Override
    public Property<Integer> getInteger(String name, Integer fallback) {
        return new Property<Integer>() {
            @Override
            public Integer get() {
                return Integer.getInteger(name, fallback);
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    @Override
    public Property<Boolean> getBoolean(String name, Boolean fallback) {
        return new Property<Boolean>() {
            @Override
            public Boolean get() {
                String v = null;
                try {
                    v = System.getProperty(name);
                } catch (IllegalArgumentException | NullPointerException e) {
                }
                if (v == null) {
                    return fallback;
                }
                return Boolean.getBoolean(name);
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    @Override
    public Property<Long> getLong(String name, Long fallback) {
        return new Property<Long>() {
            @Override
            public Long get() {
                return Long.getLong(name, fallback);
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}
