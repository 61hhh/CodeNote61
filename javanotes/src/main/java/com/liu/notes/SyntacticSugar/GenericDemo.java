package com.liu.notes.SyntacticSugar;

import java.util.*;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/18
 */
public class GenericDemo {

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "leslie");
        map.put("wechat", "sincere");
        map.put("mail", "1587403870@qq.com");

        List<Integer> listA = new ArrayList();
        List<String> listB = new ArrayList();
        System.out.println("比较参数为Integer类型和String类型的List：" + (listA.getClass() == listB.getClass()));

        // listA.add("hello");
        // listB.add(123);
        
    }

    public static <A extends Comparable<A>> A max(Collection<A> xs) {
        Iterator<A> xi = xs.iterator();
        A w = xi.next();
        while (xi.hasNext()) {
            A x = xi.next();
            if (w.compareTo(x) < 0) {
                w = x;
            }
        }
        return w;
    }
}
