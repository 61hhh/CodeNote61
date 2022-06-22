package com.liu.segment.ansj;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LiuYi
 * @description ansj分词API使用测试
 * @since 2022/6/22
 */
public class AnsjSegTest {

    /*
        多用户词典
     */
    public static void moreUserDic() {
        // 多用户词典
        String str = "超人这部电影的扮演者亨利·卡维尔,是一个魔兽迷";
        System.out.println(ToAnalysis.parse(str));
        // 添加两个词汇 超人 魔兽迷
        Forest dic1 = new Forest();
        Library.insertWord(dic1, new Value("超人", "define", "1000"));
        Forest dic2 = new Forest();
        Library.insertWord(dic2, new Value("魔兽迷", "define", "1000"));
        System.out.println("----------dic1+dic2----------");
        System.out.println(ToAnalysis.parse(str, dic1, dic2));

        System.out.println("----------删除dic1中的词----------");
        Library.removeWord(dic1, "超人");
        System.out.println(ToAnalysis.parse(str, dic1, dic2));
    }

    /*
        动态增删词库
     */
    public static void dynamicWord() {
        // 增加新词 中间按照'\t'隔开
        UserDefineLibrary.insertWord("ansj中文分词", "userDefine", 1000);
        Result result = ToAnalysis.parse("我觉得ansj中文分词是个很强大的分词组件！自卖自夸~");
        System.out.println("增加新词例子：" + result);
        // 删除词语 只能删除.用户自定义的词典
        UserDefineLibrary.removeWord("ansj中文分词");
        result = ToAnalysis.parse("我觉得ansj中文分词是个很强大的分词组件！自卖自夸~");
        System.out.println("删除用户自定义词典例子：" + result);

        // 清空词典
        UserDefineLibrary.clear();
    }

    /*
        加载词典文件
     */
    public static void localDic() {
        try {
            // 加载文件构建Forest
            Forest rootForest = Library.makeForest("library/userLibrary.dic");
            System.out.println(rootForest.toMap());

            InputStream inputStream = AnsjSegTest.class.getResourceAsStream("library/userLibrary.dic");
            Forest resourceForest = Library.makeForest(inputStream);
            String str = "我觉得ansj中文分词是个很强大的分词组件！自卖自夸~";
            Result result = ToAnalysis.parse(str, resourceForest);
            List<Term> termList = result.getTerms();
            for (Term term : termList) {
                System.out.println(term.termNatures().personAttr.flag);
                System.out.println(term.getName() + ": " + term.getNatureStr());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        基本分词
        基本就是保证了最基本的分词.词语颗粒度最非常小的..所涉及到的词大约是10万左右.
        基本分词速度非常快.在macAir上.能到300w字每秒.同时准确率也很高.但是对于新词他的功能十分有限
     */
    public static void baseAnay(String content) {
        Result result = BaseAnalysis.parse(delHTMLTag(content).replace("\n", "").replace(" ", "").replace("\t", ""));
        System.out.println("result:" + result);
    }

    /*
        精准分词:它在易用性,稳定性.准确性.以及分词效率上.都取得了一个不错的平衡.
     */
    public static void toAnay(String content) {
        Result result = ToAnalysis.parse(content);
        System.out.println("result:" + result);
    }

    /*
        nlp分词
     */
    public static void nlpAnay(String content) {
        Result result = NlpAnalysis.parse(delHTMLTag(content).replace("\n", "").replace(" ", "").replace("\t", ""));
        System.out.println("result:" + result);
        List<Term> terms = result.getTerms();
        for (Term term : terms) {
            System.out.println(term.termNatures().personAttr.flag);
            String name = term.getName();
            String nature = term.getNatureStr();
            if (nature.equals("nt") || nature.equals("nr")) {
                System.out.println("------------------");
                System.out.println("getName:" + term.getName());
                System.out.println("getNatureStr:" + term.getNatureStr());
            }
        }
    }

    public static void main(String[] args) {
        moreUserDic();
        dynamicWord();
        localDic();
        nlpAnay("张三是姓张三个兄弟的老三");
    }

    /**
     * 筛除HTML标签
     */
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }
}
