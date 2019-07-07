package me.mingshan.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * One to one mapping.
 *
 * @author mingshan
 */
public class MappingUtil {

    private MappingUtil() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }

    public static <T> boolean oneToOne(List<T> source, String fieldName1, String fieldName2) {
        Map<String, String> map1 = new HashMap<>();
        Map<String, String> map2 = new HashMap<>();
        for (T item : source) {
            String property1 = getPrivateProperty(item, fieldName1);
            String property2 = getPrivateProperty(item, fieldName2);
            if (checkMatching(map1, property1, property2)) return false;

            if (checkMatching(map2, property2, property1)) return false;
        }
        return true;
    }

    private static boolean checkMatching(Map<String, String> map1, String property1, String property2) {
        String value1 = map1.get(property1);
        if (value1 == null) {
            map1.put(property1, property2);
        } else if (!value1.equals(property2)) {
            return true;
        }
        return false;
    }

    private static <T> String getPrivateProperty(T t, String fieldName) {
        try {
            return t.getClass().getMethod("get" + upperFirstLatter(fieldName)).invoke(t).toString();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static String upperFirstLatter(String letter){
        char[] chars = letter.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char) (chars[0]-32);
        }
        return String.valueOf(chars);
    }
}
