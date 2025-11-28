package com.example.bmi_mvvm;

import android.content.Context;

/**
 * 根据 BMI 分类、性别与年龄，生成报告页需要展示的图片和建议文案。
 * 将业务逻辑集中在模型层，保证 ViewModel 只负责数据流转。
 */
public class BMIReportModel {

    /**
     * 根据分类映射到对应的图片与建议，如果分类为空则给出默认提示。
     */
    public BMIReportData getBMIReportData(Context context, String bmiCategory, String gender, int age) {
        BMIReportData data = new BMIReportData();
        boolean isChild = age < 18;

        String adultUnderweight = context.getString(R.string.bmi_category_underweight);
        String adultNormal = context.getString(R.string.bmi_category_normal);
        String adultOverweight = context.getString(R.string.bmi_category_overweight);
        String adultObese = context.getString(R.string.bmi_category_obese);

        String childSeverelyUnder = context.getString(R.string.bmi_category_child_severelyUnderweight);
        String childUnder = context.getString(R.string.bmi_category_child_Underweight);
        String childAcceptable = context.getString(R.string.bmi_category_child_AcceptableWeight);
        String childOver = context.getString(R.string.bmi_category_child_Overweight);
        String childSeverelyOver = context.getString(R.string.bmi_category_child_Severely_Overweight);

        // 没有分类时返回默认图片与文案，避免空指针。
        if (bmiCategory == null || bmiCategory.isEmpty()) {
            data.imageRes = R.drawable.bot_fit;
            data.advice = context.getString(R.string.advice_message);
            return data;
        }

        // 成人与儿童分类组合不同，统一在此做映射。
        if (bmiCategory.equals(adultUnderweight) || bmiCategory.equals(childUnder)) {
            data.imageRes = R.drawable.bot_thin;
            data.advice = getUnderweightAdvice(context, gender, isChild);
        } else if (bmiCategory.equals(adultNormal)) {
            data.imageRes = R.drawable.bot_fit;
            data.advice = getNormalWeightAdvice(context, gender, isChild);
        } else if (bmiCategory.equals(childAcceptable)) {
            data.imageRes = R.drawable.bot_fit;
            data.advice = getAcceptableWeightAdvice(context, gender);
        } else if (bmiCategory.equals(adultOverweight) || bmiCategory.equals(childOver)) {
            data.imageRes = R.drawable.bot_fat;
            data.advice = getOverweightAdvice(context, gender, isChild);
        } else if (bmiCategory.equals(adultObese)) {
            data.imageRes = R.drawable.bot_fat;
            data.advice = getObeseAdvice(context, gender, isChild);
        } else if (bmiCategory.equals(childSeverelyUnder)) {
            data.imageRes = R.drawable.bot_thin;
            data.advice = getSeverelyUnderweightAdvice(context, gender, isChild);
        } else if (bmiCategory.equals(childSeverelyOver)) {
            data.imageRes = R.drawable.bot_fat;
            data.advice = getSeverelyOverweightAdvice(context, gender, isChild);
        } else {
            data.imageRes = R.drawable.bot_fit;
            data.advice = context.getString(R.string.advice_message);
        }
        return data;
    }

    // 以下私有方法仅负责返回文字建议
    private String getUnderweightAdvice(Context context, String gender, boolean isChild) {
        if (isChild) return context.getString(R.string.child_is_underweight);
        else return context.getString(R.string.underweight);
    }

    private String getNormalWeightAdvice(Context context, String gender, boolean isChild) {
        if (isChild) return context.getString(R.string.child_has_healthy_weight);
        else return context.getString(R.string.healthy_weight);
    }

    private String getOverweightAdvice(Context context, String gender, boolean isChild) {
        if (isChild) return context.getString(R.string.child_is_overweight);
        else return context.getString(R.string.overweight);
    }

    private String getObeseAdvice(Context context, String gender, boolean isChild) {
        if (isChild) return context.getString(R.string.child_is_obese);
        else return context.getString(R.string.obese);
    }

    private String getSeverelyUnderweightAdvice(Context context, String gender, boolean isChild) {
        return context.getString(R.string.severely_underweight);
    }

    private String getAcceptableWeightAdvice(Context context, String gender) {
        return context.getString(R.string.acceptable_weight);
    }

    private String getSeverelyOverweightAdvice(Context context, String gender, boolean isChild) {
        return context.getString(R.string.severely_overweight);
    }
}
