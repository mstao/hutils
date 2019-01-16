package me.mingshan.util;

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test.func1(10));
        System.out.println(test.func2(10, 1));
    }

    /**
     * 阶乘递归
     *
     * @param n
     * @return
     */
    public int func1(int n) {
        if (n == 1) return 1;
        return n * func1(n - 1);
    }

    /**
     * 尾递归
     *
     * @param n
     * @param total
     * @return
     */
    public int func2(int n, int total) {
        if (n == 1) return total;
        return func2(n - 1, n * total);
    }
}
