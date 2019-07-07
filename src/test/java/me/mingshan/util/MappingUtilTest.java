package me.mingshan.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static me.mingshan.util.MappingUtil.oneToOne;

/**
 * @author mingshan
 */
public class MappingUtilTest {

    @Test
    public void testOneToOne() {
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
