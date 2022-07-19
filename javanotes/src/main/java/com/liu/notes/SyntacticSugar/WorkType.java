package com.liu.notes.SyntacticSugar;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/19
 */
public enum WorkType {
    STUDENT("学生"),
    TEACHER("老师");

    private String name;

    WorkType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static void main(String[] args) {
        // 直接通过枚举值输出
        System.out.println(WorkType.STUDENT.name);
        // 通过valueOf获取到对应枚举值
        System.out.println(WorkType.valueOf("TEACHER").getName());
        // 通过values获取到所有的枚举值
        WorkType[] values = WorkType.values();
        for (WorkType value : values) {
            System.out.println("values()方法获取到：" + value.getName());
        }
    }
}
