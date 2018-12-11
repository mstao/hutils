package me.mingshan.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Provides common methods to handle property.
 *
 * @author mingshan
 */
public class PropertyUtil {

    private PropertyUtil() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }

    /**
     * Loads property file information.
     *
     * @param fileName the filname
     * @return {@link Properties}
     */
    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        InputStream input = ClassUtil.getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            return properties;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

        try {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return properties;
    }
}
