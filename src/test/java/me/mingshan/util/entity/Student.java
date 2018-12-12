package me.mingshan.util.entity;

/**
 * @author mingshan
 */
public class Student extends Person {
    private String address;
    public int grade;
    private static final String aaa = "abc";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "age=" + age +
                ", address='" + address + '\'' +
                ", grade=" + grade +
                '}';
    }
}