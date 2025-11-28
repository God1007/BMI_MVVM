package com.example.bmi_mvvm; // 指定包名，声明报告模型所在命名空间

import android.content.Context; // 导入 Context 以访问 SharedPreferences
import android.content.SharedPreferences; // 导入 SharedPreferences 读取保存的数据

/**
 * 为报告页提供最近一次计算的 BMI 结果，并保存用户在报告页选择的记录。
 */
public class BMIReportModel { // 定义 BMIReportModel 类

    private static final String PREF_NAME = BMIModel.PREF_NAME; // 复用 BMIModel 的偏好文件名
    private final SharedPreferences sharedPreferences; // SharedPreferences 实例

    public BMIReportModel(Context context) { // 构造函数接收 Context
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE); // 初始化 SharedPreferences
    } // 构造函数结束

    public BMIReportData loadLastReport(Context context) { // 读取最近一次 BMI 结果
        String bmiValue = sharedPreferences.getString(BMIModel.KEY_BMI, ""); // 读取 BMI 数值
        String bmiCategory = sharedPreferences.getString(BMIModel.KEY_CATEGORY, ""); // 读取 BMI 分类
        String height = sharedPreferences.getString(BMIModel.KEY_HEIGHT, ""); // 读取身高
        String weight = sharedPreferences.getString(BMIModel.KEY_WEIGHT, ""); // 读取体重
        String age = sharedPreferences.getString(BMIModel.KEY_AGE, ""); // 读取年龄
        String gender = sharedPreferences.getString(BMIModel.KEY_GENDER, context.getString(R.string.male)); // 读取性别
        return new BMIReportData(bmiValue, bmiCategory, height, weight, age, gender); // 返回封装好的数据对象
    } // loadLastReport 结束

    public void saveReport(BMIReportData data) { // 保存当前报告的数据
        SharedPreferences.Editor editor = sharedPreferences.edit(); // 获取编辑器
        editor.putString(BMIModel.KEY_BMI, data.getBmiValue()); // 保存 BMI 数值
        editor.putString(BMIModel.KEY_CATEGORY, data.getBmiCategory()); // 保存 BMI 分类
        editor.putString(BMIModel.KEY_HEIGHT, data.getHeight()); // 保存身高
        editor.putString(BMIModel.KEY_WEIGHT, data.getWeight()); // 保存体重
        editor.putString(BMIModel.KEY_AGE, data.getAge()); // 保存年龄
        editor.putString(BMIModel.KEY_GENDER, data.getGender()); // 保存性别
        editor.apply(); // 异步提交
    } // saveReport 结束
} // BMIReportModel 类结束
