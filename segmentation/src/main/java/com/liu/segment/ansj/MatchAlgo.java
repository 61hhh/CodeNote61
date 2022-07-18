package com.liu.segment.ansj;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Description:
 * 中文分词算法之--最大匹配法 - Yolanda的文章 - 知乎https://zhuanlan.zhihu.com/p/103392455
 *
 * @author LiuYi
 * @since 2022/7/18
 */
public class MatchAlgo {

    public static final int MAX_LEN = 5; // 假定最大长度固定 而不是从词典中获取
    public static final List<String> dict = new ArrayList<>(); // 匹配的词典

    static {
        dict.add("我们");
        dict.add("在");
        dict.add("在野");
        dict.add("野生");
        dict.add("生动");
        dict.add("动物园");
        dict.add("野生动物园");
        dict.add("物");
        dict.add("园");
        dict.add("玩");
        dict.add("耍");
        dict.add("玩耍");
        dict.add("的");
        dict.add("很");
        dict.add("开");
        dict.add("心");
        dict.add("开心");
        dict.add("很开心");
    }

    /**
     * 正向最大匹配
     * 基于词典
     *
     * @param text
     * @return
     */
    public static List<String> forwardMaxMatch(String text) {
        ArrayList<String> result = new ArrayList<>();
        while (text.length() > 0) {
            // len:匹配的长度 当文本已经不足MAX_LEN时，取文本自身长度
            int len = MAX_LEN;
            if (text.length() < MAX_LEN) {
                len = text.length();
            }
            // 正向匹配 从0开始取len长度
            String tryWord = text.substring(0, len);
            while (!dict.contains(tryWord)) {
                if (tryWord.length() == 1) {// 长度为1且没有匹配的词 直接返回
                    break;
                }
                tryWord = tryWord.substring(0, tryWord.length() - 1);
            }
            result.add(tryWord);
            // 移除头部匹配上的tryWord 继续循环
            text = text.substring(tryWord.length());
        }
        return result;
    }


    /**
     * 逆向最大匹配：
     * 基于词典，从后往前
     */
    public static List<String> reverseMaxMatch(String text) {
        Stack<String> result = new Stack<>();
        while (text.length() > 0) {
            // len:匹配的长度 当文本已经不足MAX_LEN时，取文本自身长度
            int len = MAX_LEN;
            if (text.length() < MAX_LEN) {
                len = text.length();
            }
            // 取指定的最大长度
            String tryWord = text.substring(text.length() - len);
            while (!dict.contains(tryWord)) {//词典中不包含该文本
                if (tryWord.length() == 1) {
                    // 长度为1且没有匹配的词 直接返回
                    break;
                }
                // 长度减一 继续匹配
                tryWord = tryWord.substring(1);
            }
            result.add(tryWord);
            // 移除末尾的这个tryWord 继续循环
            text = text.substring(0, text.length() - tryWord.length());
        }
        int size = result.size();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(result.pop());
        }
        return list;
    }

    public static void main(String[] args) {
        List<String> list = reverseMaxMatch("我们在野生动物园玩耍的很开心");
        for (String str : list) {
            System.out.print(str + "/");
        }
        System.out.println("");

        List<String> list1 = forwardMaxMatch("我们在野生动物园玩耍的很开心");
        for (String str : list1) {
            System.out.print(str + "/");
        }

    }
}






















