package com.liu.notes.SyntacticSugar;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/19
 */
public class BoxDemo {

    /*
     * 装箱 调用包装类 BoxClass.valueOf() 封装基本类型值
     * Integer.valueOf(i)
     */
    public static void boxTest() {
        int i = 10;
        Integer num = i;
    }

    /*
     * 拆箱 调用包装类的xxxValue()获取包装类的值
     * num.intValue()
     */
    public static void unboxTest() {
        Integer num = 10;
        int i = num;
    }
}
