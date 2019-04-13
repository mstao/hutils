package me.mingshan.util;

import me.mingshan.util.entity.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;

public class Test {
    private Map<Long, Long> cache = new HashMap<>();

    public static void main(String[] args) {
        Test test = new Test();
        //System.out.println(test.func1(100_000));
        //System.out.println(test.func2(100_000, 1));
        //System.out.println(factorial(100_000));

        Stream.iterate(1, item -> item + 1).limit(10).forEach(System.out::println);
    }

    /**
     * 阶乘递归
     *
     * @param n
     * @return
     */
    public long func1(long n) {
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
    public long func2(long n, long total) {
        if (n == 1) return total;
        return func2(n - 1, n * total);
    }

    public long fib(long n) {
        if (n == 1 || n == 2) {
            return 1;
        }

        return fib(n - 1) + fib(n - 2);
    }


    public long fib2(long n) {
        if (n == 1 || n == 2) {
            return 1;
        }

        if (cache.containsKey(n)) {
            return cache.get(n);
        }

        long res = fib(n - 1) + fib(n - 2);
        cache.put(n, res);
        return res;
    }

    public static TailRecursion<Long> factorialTailRecursion(final long n, final long total) {
        if (n == 1)
            return TailInvoke.done(total);
        else
            return TailInvoke.call(() -> factorialTailRecursion(n - 1, n * total));
    }

    public static long factorial(final long number) {
        return factorialTailRecursion(number, 1).invoke();
    }


    @org.junit.Test
    public void test1() {
        Person person1 = new Person();
        person1.setName("1");
        person1.setState(true);
        Person person2 = new Person();
        person2.setName("2");
        person2.setState(false);
        Person person3 = new Person();
        person3.setName("3");
        person3.setState(true);
        Person person4 = new Person();
        person4.setName("4");
        person4.setState(true);
        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);

        List<Person> st = new ArrayList<>();
        persons
            .stream()
            .collect(partitioningBy(Person::isState))
            .forEach((k, v) -> {
                // 如果key为true
                if (k) {
                    v.forEach(item -> item.setState(false));
                    v.get(v.size() - 1).setState(true);
                    st.addAll(v);
                } else {
                    st.addAll(v);
                }
            });

        System.out.println(st);


        List<Person> tempList = new ArrayList<>(persons.size());
        persons
            .parallelStream()
            .collect(Collectors.groupingBy(Person::getType))
            .forEach((k, v) -> {
                int[] lastIndex = new int[1];
                for (int i = 1; i <= v.size(); i++) {
                    Person item = v.get(i - 1);
                    if (item.isState()) {
                        int index = lastIndex[0];
                        if (index != 0) {
                            v.get(index - 1).setState(false);
                        }
                        lastIndex[0] = i;
                    }
                }
                tempList.addAll(v);
                lastIndex = null;// help gc
            });
    }
}
