package me.mingshan.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * One to one mapping.
 *
 * @author mingshan
 */
public class MappingUtil {

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

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();

        Student s1 = new Student();
        s1.setId("111");
        s1.setName("qq");
        s1.setClassName("c1");


        Student s2 = new Student();
        s2.setId("2222");
        s2.setName("qq12");
        s2.setClassName("c1");

        Student s3 = new Student();
        s3.setId("3333");
        s3.setName("ww");
        s3.setClassName("c3");

        students.add(s1);
        students.add(s2);
        students.add(s3);

        boolean b = oneToOne(students, "name", "className");
        if (!b) {
            System.out.println("name 和 className 不满足一一对应关系");
        }
    }


    static class Student {
        private String id;
        private String name;
        private String className;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }
}
