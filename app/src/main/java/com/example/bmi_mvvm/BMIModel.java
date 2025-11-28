package com.example.bmi_mvvm;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 负责 BMI 计算、分类判定以及用户输入与结果的本地持久化逻辑。
 * 通过 SharedPreferences 保存最近一次输入与结果，方便再次打开时直接填充。
 */
public class BMIModel {

    // SharedPreferences 文件名与键值常量，方便统一管理。
    public static final String PREF_NAME = "BMI_Data";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_AGE = "age";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_BMI = "bmi";
    public static final String KEY_CATEGORY = "bmi_category";

    /**
     * SharedPreferences 负责保存用户输入及计算结果。
     */
    private final SharedPreferences sharedPreferences;
    /**
     * 保存应用级 Context，避免持有 Activity 引用导致内存泄漏。
     */
    private final Context context;

    public BMIModel(Context context) {
        this.context = context.getApplicationContext();
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * BMI 计算公式：体重(kg) / 身高(m)^2。传入厘米需转换为米。
     */
    public double calculateBMI(double heightCm, double weightKg) {
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    /**
     * 成人 BMI 分类：按照亚洲标准将 BMI 区间映射成对应的文本类别。
     */
    public String getAdultBMICategory(double bmi, Context context) {
        if (bmi >= 25.0) return context.getString(R.string.bmi_category_obese);
        else if (bmi >= 23.0) return context.getString(R.string.bmi_category_overweight);
        else if (bmi >= 18.5) return context.getString(R.string.bmi_category_normal);
        else return context.getString(R.string.bmi_category_underweight);
    }

    /**
     * 儿童 BMI 分类：根据年龄范围与性别选择男/女对应的判定表。
     */
    public String getChildBMICategory(double bmi, int age, String gender, Context context) {
        if (age < 6 || age > 18) return context.getString(R.string.out_of_old_range);
        if (gender.equals(context.getString(R.string.male))) return getBoyBMICategory(bmi, age, context);
        else return getGirlBMICategory(bmi, age, context);
    }

    /**
     * 针对男童的 BMI 判定，每个年龄对应一组阈值。
     */
    private String getBoyBMICategory(double bmi, int age, Context context) {
        switch (age) {
            case 6: return context.getString(categorizeChildBMI(bmi, 12.8, 13.1, 18.8, 21.4));
            case 7: return context.getString(categorizeChildBMI(bmi, 13.0, 13.3, 19.8, 23.0));
            case 8: return context.getString(categorizeChildBMI(bmi, 13.2, 13.6, 20.9, 24.6));
            case 9: return context.getString(categorizeChildBMI(bmi, 13.5, 13.8, 21.8, 26.0));
            case 10: return context.getString(categorizeChildBMI(bmi, 13.8, 14.1, 22.7, 27.3));
            case 11: return context.getString(categorizeChildBMI(bmi, 14.1, 14.5, 23.6, 28.3));
            case 12: return context.getString(categorizeChildBMI(bmi, 14.4, 14.8, 24.3, 29.2));
            case 13: return context.getString(categorizeChildBMI(bmi, 14.7, 15.1, 25.0, 30.0));
            case 14: return context.getString(categorizeChildBMI(bmi, 15.0, 15.4, 25.5, 30.6));
            case 15: return context.getString(categorizeChildBMI(bmi, 15.3, 15.8, 26.1, 31.2));
            case 16: return context.getString(categorizeChildBMI(bmi, 15.6, 16.1, 26.5, 31.7));
            case 17: return context.getString(categorizeChildBMI(bmi, 15.9, 16.3, 27.0, 32.1));
            case 18: return context.getString(categorizeChildBMI(bmi, 16.1, 16.6, 27.4, 32.4));
            default: return context.getString(R.string.bmi_category_error);
        }
    }

    /**
     * 针对女童的 BMI 判定，阈值与男童不同。
     */
    private String getGirlBMICategory(double bmi, int age, Context context) {
        switch (age) {
            case 6: return context.getString(categorizeChildBMI(bmi, 12.6, 12.8, 18.3, 20.5));
            case 7: return context.getString(categorizeChildBMI(bmi, 12.8, 13.1, 19.1, 21.8));
            case 8: return context.getString(categorizeChildBMI(bmi, 13.1, 13.4, 20.1, 23.1));
            case 9: return context.getString(categorizeChildBMI(bmi, 13.4, 13.7, 21.0, 24.4));
            case 10: return context.getString(categorizeChildBMI(bmi, 13.7, 14.1, 21.9, 25.6));
            case 11: return context.getString(categorizeChildBMI(bmi, 14.1, 14.4, 22.7, 26.6));
            case 12: return context.getString(categorizeChildBMI(bmi, 14.4, 14.8, 23.4, 27.5));
            case 13: return context.getString(categorizeChildBMI(bmi, 14.8, 15.2, 24.0, 28.3));
            case 14: return context.getString(categorizeChildBMI(bmi, 15.1, 15.5, 24.6, 28.9));
            case 15: return context.getString(categorizeChildBMI(bmi, 15.4, 15.8, 25.0, 29.4));
            case 16: return context.getString(categorizeChildBMI(bmi, 15.7, 16.1, 25.4, 29.7));
            case 17: return context.getString(categorizeChildBMI(bmi, 15.9, 16.3, 25.7, 30.0));
            case 18: return context.getString(categorizeChildBMI(bmi, 16.1, 16.5, 25.9, 30.3));
            default: return context.getString(R.string.bmi_category_error);
        }
    }

    /**
     * 根据 BMI 与给定阈值返回对应的字符串资源 id，供男女童方法复用。
     */
    private int categorizeChildBMI(double bmi, double severeUnder, double under, double acceptable, double over) {
        if (bmi <= severeUnder) return R.string.bmi_category_child_severelyUnderweight;
        else if (bmi <= under) return R.string.bmi_category_child_Underweight;
        else if (bmi <= acceptable) return R.string.bmi_category_child_AcceptableWeight;
        else if (bmi <= over) return R.string.bmi_category_child_Overweight;
        else return R.string.bmi_category_child_Severely_Overweight;
    }

    // SharedPreferences
    /**
     * 将当前输入的身高、体重、年龄和性别持久化，便于下次进入自动填充。
     */
    public void saveData(String height, String weight, String age, String gender) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_HEIGHT, height);
        editor.putString(KEY_WEIGHT, weight);
        editor.putString(KEY_AGE, age);
        editor.putString(KEY_GENDER, gender);
        editor.apply();
    }

    /**
     * 读取最近一次保存的输入，按固定顺序返回字符串数组。
     */
    public String[] loadData() {
        String height = sharedPreferences.getString(KEY_HEIGHT, "");
        String weight = sharedPreferences.getString(KEY_WEIGHT, "");
        String age = sharedPreferences.getString(KEY_AGE, "");
        String gender = sharedPreferences.getString(KEY_GENDER, context.getString(R.string.male));
        return new String[]{height, weight, age, gender};
    }

    /**
     * 保存最新计算的 BMI 数值与分类结果。
     */
    public void saveResult(String bmi, String category) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_BMI, bmi);
        editor.putString(KEY_CATEGORY, category);
        editor.apply();
    }

    /**
     * 读取最近一次的 BMI 数值，如果不存在则返回空字符串。
     */
    public String getLastBmi() {
        return sharedPreferences.getString(KEY_BMI, "");
    }

    /**
     * 读取最近一次的 BMI 分类结果。
     */
    public String getLastCategory() {
        return sharedPreferences.getString(KEY_CATEGORY, "");
    }
}
