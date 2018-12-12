package me.mingshan.util;

import me.mingshan.util.entity.Student;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author mingshan
 */
public class ReflectUtilTest {

    @Test
    public void testFeild1() {

        try {
            Field field1 = ReflectUtil.getDeclaredField(Student.class, "address");
            field1.setAccessible(true);
            Assert.assertEquals("address", field1.getName());

            Field[] allFields = ReflectUtil.getAllFields(Student.class);
            for (Field field : allFields) {
                field.setAccessible(true);
                System.out.println(field.getName());
            }

            String aaa = ReflectUtil.getConstValue(Student.class, "aaa");
            Assert.assertEquals("abc", aaa);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
