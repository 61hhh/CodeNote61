package com.liu.notes.SyntacticSugar;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/19
 */
public class variableParam {

    public static void main(String[] args) {
        printMsg("leslie","China","123456@qq.com","fantasy");
    }

    public static void printMsg(String ... args){
        for (String str : args) {
            System.out.println("str-----> "+str);
        }
    }
}
