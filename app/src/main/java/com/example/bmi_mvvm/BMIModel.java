package com.example.bmi_mvvm; // 定义包名，确保类在正确的命名空间

import android.content.Context; // 导入 Context 以访问资源与存储
import android.content.SharedPreferences; // 导入 SharedPreferences 用于本地持久化

/**
 * 负责 BMI 计算、分类判定以及用户输入与结果的本地持久化逻辑。
 * 通过 SharedPreferences 保存最近一次输入与结果，方便再次打开时直接填充。
 */
public class BMIModel { // 定义 BMIModel 类，封装业务逻辑

    public static final String PREF_NAME = "BMI_Data"; // SharedPreferences 文件名常量
    public static final String KEY_HEIGHT = "height"; // 存储身高的键
    public static final String KEY_WEIGHT = "weight"; // 存储体重的键
    public static final String KEY_AGE = "age"; // 存储年龄的键
    public static final String KEY_GENDER = "gender"; // 存储性别的键
    public static final String KEY_BMI = "bmi"; // 存储 BMI 数值的键
    public static final String KEY_CATEGORY = "bmi_category"; // 存储 BMI 分类的键

    private final SharedPreferences sharedPreferences; // SharedPreferences 实例用于持久化数据
    private final Context context; // 应用级 Context，避免内存泄漏

    public BMIModel(Context context) { // 构造函数接收 Context
        this.context = context.getApplicationContext(); // 保存应用级 Context
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE); // 初始化 SharedPreferences
    } // 构造函数结束

    public double calculateBMI(double heightCm, double weightKg) { // BMI 计算：接受厘米与公斤
        double heightM = heightCm / 100.0; // 将身高从厘米转换为米
        return weightKg / (heightM * heightM); // 按公式体重 / (身高^2) 得到 BMI
    } // calculateBMI 结束

    public String getAdultBMICategory(double bmi, Context context) { // 获取成人 BMI 分类
        if (bmi >= 25.0) return context.getString(R.string.bmi_category_obese); // BMI ≥ 25 判定为肥胖
        else if (bmi >= 23.0) return context.getString(R.string.bmi_category_overweight); // 23-25 判定为超重
        else if (bmi >= 18.5) return context.getString(R.string.bmi_category_normal); // 18.5-23 判定为正常
        else return context.getString(R.string.bmi_category_underweight); // 低于 18.5 判定为偏瘦
    } // getAdultBMICategory 结束

    public String getChildBMICategory(double bmi, int age, String gender, Context context) { // 获取儿童 BMI 分类
        if (age < 6 || age > 18) return context.getString(R.string.out_of_old_range); // 超出年龄范围直接返回提示
        if (gender.equals(context.getString(R.string.male))) return getBoyBMICategory(bmi, age, context); // 男童调用男童规则
        else return getGirlBMICategory(bmi, age, context); // 女童调用女童规则
    } // getChildBMICategory 结束

    private String getBoyBMICategory(double bmi, int age, Context context) { // 男童分类判定
        switch (age) { // 根据年龄选择阈值
            case 6: return context.getString(categorizeChildBMI(bmi, 12.8, 13.1, 18.8, 21.4)); // 6 岁对应阈值
            case 7: return context.getString(categorizeChildBMI(bmi, 13.0, 13.3, 19.8, 23.0)); // 7 岁对应阈值
            case 8: return context.getString(categorizeChildBMI(bmi, 13.2, 13.6, 20.9, 24.6)); // 8 岁对应阈值
            case 9: return context.getString(categorizeChildBMI(bmi, 13.5, 13.8, 21.8, 26.0)); // 9 岁对应阈值
            case 10: return context.getString(categorizeChildBMI(bmi, 13.8, 14.1, 22.7, 27.3)); // 10 岁对应阈值
            case 11: return context.getString(categorizeChildBMI(bmi, 14.1, 14.5, 23.6, 28.3)); // 11 岁对应阈值
            case 12: return context.getString(categorizeChildBMI(bmi, 14.4, 14.8, 24.3, 29.2)); // 12 岁对应阈值
            case 13: return context.getString(categorizeChildBMI(bmi, 14.7, 15.1, 25.0, 30.0)); // 13 岁对应阈值
            case 14: return context.getString(categorizeChildBMI(bmi, 15.0, 15.4, 25.5, 30.6)); // 14 岁对应阈值
            case 15: return context.getString(categorizeChildBMI(bmi, 15.3, 15.8, 26.1, 31.2)); // 15 岁对应阈值
            case 16: return context.getString(categorizeChildBMI(bmi, 15.6, 16.1, 26.5, 31.7)); // 16 岁对应阈值
            case 17: return context.getString(categorizeChildBMI(bmi, 15.9, 16.3, 27.0, 32.1)); // 17 岁对应阈值
            case 18: return context.getString(categorizeChildBMI(bmi, 16.1, 16.6, 27.4, 32.4)); // 18 岁对应阈值
            default: return context.getString(R.string.bmi_category_error); // 异常年龄返回错误
        } // switch 结束
    } // getBoyBMICategory 结束

    private String getGirlBMICategory(double bmi, int age, Context context) { // 女童分类判定
        switch (age) { // 根据年龄选择阈值
            case 6: return context.getString(categorizeChildBMI(bmi, 12.6, 12.8, 18.3, 20.5)); // 6 岁阈值
            case 7: return context.getString(categorizeChildBMI(bmi, 12.8, 13.1, 19.1, 21.8)); // 7 岁阈值
            case 8: return context.getString(categorizeChildBMI(bmi, 13.1, 13.4, 20.1, 23.1)); // 8 岁阈值
            case 9: return context.getString(categorizeChildBMI(bmi, 13.4, 13.7, 21.0, 24.4)); // 9 岁阈值
            case 10: return context.getString(categorizeChildBMI(bmi, 13.7, 14.1, 21.9, 25.6)); // 10 岁阈值
            case 11: return context.getString(categorizeChildBMI(bmi, 14.1, 14.4, 22.7, 26.6)); // 11 岁阈值
            case 12: return context.getString(categorizeChildBMI(bmi, 14.4, 14.8, 23.4, 27.5)); // 12 岁阈值
            case 13: return context.getString(categorizeChildBMI(bmi, 14.8, 15.2, 24.0, 28.3)); // 13 岁阈值
            case 14: return context.getString(categorizeChildBMI(bmi, 15.1, 15.5, 24.6, 28.9)); // 14 岁阈值
            case 15: return context.getString(categorizeChildBMI(bmi, 15.4, 15.8, 25.0, 29.4)); // 15 岁阈值
            case 16: return context.getString(categorizeChildBMI(bmi, 15.7, 16.1, 25.4, 29.7)); // 16 岁阈值
            case 17: return context.getString(categorizeChildBMI(bmi, 15.9, 16.3, 25.7, 30.0)); // 17 岁阈值
            case 18: return context.getString(categorizeChildBMI(bmi, 16.1, 16.5, 25.9, 30.3)); // 18 岁阈值
            default: return context.getString(R.string.bmi_category_error); // 异常年龄返回错误
        } // switch 结束
    } // getGirlBMICategory 结束

    private int categorizeChildBMI(double bmi, double severeUnder, double under, double acceptable, double over) { // 根据阈值返回类别资源 id
        if (bmi <= severeUnder) return R.string.bmi_category_child_severelyUnderweight; // 极度偏瘦
        else if (bmi <= under) return R.string.bmi_category_child_Underweight; // 偏瘦
        else if (bmi <= acceptable) return R.string.bmi_category_child_AcceptableWeight; // 正常
        else if (bmi <= over) return R.string.bmi_category_child_Overweight; // 超重
        else return R.string.bmi_category_child_Severely_Overweight; // 严重超重
    } // categorizeChildBMI 结束

    public void saveData(String height, String weight, String age, String gender) { // 保存身高体重等输入
        SharedPreferences.Editor editor = sharedPreferences.edit(); // 获取编辑器
        editor.putString(KEY_HEIGHT, height); // 写入身高
        editor.putString(KEY_WEIGHT, weight); // 写入体重
        editor.putString(KEY_AGE, age); // 写入年龄
        editor.putString(KEY_GENDER, gender); // 写入性别
        editor.apply(); // 异步提交保存
    } // saveData 结束

    public String[] loadData() { // 读取保存的输入数据
        String height = sharedPreferences.getString(KEY_HEIGHT, ""); // 读取身高，默认空字符串
        String weight = sharedPreferences.getString(KEY_WEIGHT, ""); // 读取体重，默认空字符串
        String age = sharedPreferences.getString(KEY_AGE, ""); // 读取年龄，默认空字符串
        String gender = sharedPreferences.getString(KEY_GENDER, context.getString(R.string.male)); // 读取性别，默认男性
        return new String[]{height, weight, age, gender}; // 按顺序返回数组
    } // loadData 结束

    public void saveResult(String bmi, String category) { // 保存最新 BMI 结果
        SharedPreferences.Editor editor = sharedPreferences.edit(); // 获取编辑器
        editor.putString(KEY_BMI, bmi); // 存储 BMI 数值
        editor.putString(KEY_CATEGORY, category); // 存储 BMI 分类
        editor.apply(); // 异步提交保存
    } // saveResult 结束

    public String getLastBmi() { // 获取最近一次 BMI 数值
        return sharedPreferences.getString(KEY_BMI, ""); // 读取 BMI，默认为空
    } // getLastBmi 结束

    public String getLastCategory() { // 获取最近一次 BMI 分类
        return sharedPreferences.getString(KEY_CATEGORY, ""); // 读取分类，默认为空
    } // getLastCategory 结束
} // BMIModel 类结束
