package com.example.bmi_mvvm; // 指定包名，定义实体所属命名空间

/**
 * 表示一条 BMI 历史记录的简单模型，包含日期与 BMI 数值。
 */
public class BMIRecord { // 定义 BMIRecord 类
    private final String date; // 记录日期字符串
    private final float bmi; // 记录 BMI 数值

    public BMIRecord(String date, float bmi) { // 构造函数接收日期和 BMI
        this.date = date; // 初始化日期字段
        this.bmi = bmi; // 初始化 BMI 字段
    } // 构造函数结束

    public String getDate() { // 获取日期的方法
        return date; // 返回存储的日期
    } // getDate 结束

    public float getBmi() { // 获取 BMI 数值的方法
        return bmi; // 返回存储的 BMI
    } // getBmi 结束
} // BMIRecord 类结束
