package com.example.bmi_mvvm;

import android.content.Context;
import android.content.SharedPreferences;

public class BMIModel {

    private final SharedPreferences sharedPreferences;
    private final Context context;

    public BMIModel(Context context) {
        this.context = context.getApplicationContext();
        sharedPreferences = this.context.getSharedPreferences("BMI_Data", Context.MODE_PRIVATE);
    }

    // BMI 计算
    public double calculateBMI(double heightCm, double weightKg) {
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    // 成人 BMI 分类
    public String getAdultBMICategory(double bmi, Context context) {
        if (bmi >= 25.0) return context.getString(R.string.bmi_category_obese);
        else if (bmi >= 23.0) return context.getString(R.string.bmi_category_overweight);
        else if (bmi >= 18.5) return context.getString(R.string.bmi_category_normal);
        else return context.getString(R.string.bmi_category_underweight);
    }

    // 儿童 BMI 分类
    public String getChildBMICategory(double bmi, int age, String gender, Context context) {
        if (age < 6 || age > 18) return context.getString(R.string.out_of_old_range);
        if (gender.equals(context.getString(R.string.male))) return getBoyBMICategory(bmi, age, context);
        else return getGirlBMICategory(bmi, age, context);
    }

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

    private int categorizeChildBMI(double bmi, double severeUnder, double under, double acceptable, double over) {
        if (bmi <= severeUnder) return R.string.bmi_category_child_severelyUnderweight;
        else if (bmi <= under) return R.string.bmi_category_child_Underweight;
        else if (bmi <= acceptable) return R.string.bmi_category_child_AcceptableWeight;
        else if (bmi <= over) return R.string.bmi_category_child_Overweight;
        else return R.string.bmi_category_child_Severely_Overweight;
    }

    // SharedPreferences
    public void saveData(String height, String weight, String age, String gender) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("height", height);
        editor.putString("weight", weight);
        editor.putString("age", age);
        editor.putString("gender", gender);
        editor.apply();
    }

    public String[] loadData() {
        String height = sharedPreferences.getString("height", "");
        String weight = sharedPreferences.getString("weight", "");
        String age = sharedPreferences.getString("age", "");
        String gender = sharedPreferences.getString("gender", context.getString(R.string.male));
        return new String[]{height, weight, age, gender};
    }
}
