package me.mingshan.util.property;

/**
 * Interface that providers the common methods to get configuration.
 *
 * @author mingshan
 * @param <T> the generics class
 */
public interface Property<T> {
    /**
     * Gets the value of property.
     *
     * @return the value
     */
    T get();

    /**
     * Gets the name of property.
     *
     * @return the name
     */
    String getName();
}
