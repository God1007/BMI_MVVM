package com.example.bmi_mvvm; // 指定包名，声明数据持有类所属命名空间

/**
 * 封装 BMI 报告页面需要展示的数据，便于通过 Intent 传递。
 */
public class BMIReportData { // 定义 BMIReportData 类
    private final String bmiValue; // BMI 数值字符串
    private final String bmiCategory; // BMI 分类描述
    private final String height; // 身高字符串
    private final String weight; // 体重字符串
    private final String age; // 年龄字符串
    private final String gender; // 性别字符串

    public BMIReportData(String bmiValue, String bmiCategory, String height, String weight, String age, String gender) { // 构造函数接收所有字段
        this.bmiValue = bmiValue; // 初始化 BMI 数值
        this.bmiCategory = bmiCategory; // 初始化 BMI 分类
        this.height = height; // 初始化身高
        this.weight = weight; // 初始化体重
        this.age = age; // 初始化年龄
        this.gender = gender; // 初始化性别
    } // 构造函数结束

    public String getBmiValue() { return bmiValue; } // 获取 BMI 数值
    public String getBmiCategory() { return bmiCategory; } // 获取 BMI 分类
    public String getHeight() { return height; } // 获取身高
    public String getWeight() { return weight; } // 获取体重
    public String getAge() { return age; } // 获取年龄
    public String getGender() { return gender; } // 获取性别
} // BMIReportData 类结束
