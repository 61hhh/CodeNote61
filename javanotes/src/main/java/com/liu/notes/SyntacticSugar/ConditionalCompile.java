package com.liu.notes.SyntacticSugar;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/19
 */
public class ConditionalCompile {

    public static void main(String[] args) {
        final Boolean DEBUG = true;
        if (DEBUG) {
            System.out.println("DEBUG is running!");
        }
        final Boolean ERROR = false;
        if (ERROR) {
            System.out.println("ERROR!!!");
        }
    }
}
