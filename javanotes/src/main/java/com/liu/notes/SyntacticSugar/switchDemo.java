package com.liu.notes.SyntacticSugar;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/18
 */
public class switchDemo {

    public static void main(String[] args) {
        String str = "hello";
        switch (str){
            case "hello":
                System.out.println("hello");
                break;
            case "world":
                System.out.println("world");
                break;
            default:
                System.out.println("NULL");
                break;
        }
    }


}
