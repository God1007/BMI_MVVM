package com.example.bmi_mvvm;

import android.content.Context;

public class BMIReportModel {

    public BMIReportData getBMIReportData(Context context, String bmiCategory, String gender, int age) {
        BMIReportData data = new BMIReportData();
        boolean isChild = age < 18;

        switch (bmiCategory) {
            case "Underweight":
                data.imageRes = R.drawable.bot_thin;
                data.advice = getUnderweightAdvice(context, gender, isChild);
                break;
            case "Normal Range":
                data.imageRes = R.drawable.bot_fit;
                data.advice = getNormalWeightAdvice(context, gender, isChild);
                break;
            case "Overweight":
                data.imageRes = R.drawable.bot_fat;
                data.advice = getOverweightAdvice(context, gender, isChild);
                break;
            case "Obese":
                data.imageRes = R.drawable.bot_fat;
                data.advice = getObeseAdvice(context, gender, isChild);
                break;
            case "Severely Underweight":
            case "child_Underweight":
                data.imageRes = R.drawable.bot_thin;
                data.advice = getSeverelyUnderweightAdvice(context, gender, isChild);
                break;
            case "Acceptable Weight":
                data.imageRes = R.drawable.bot_fit;
                data.advice = getAcceptableWeightAdvice(context, gender);
                break;
            case "Severely Overweight":
            case "child_Overweight":
                data.imageRes = R.drawable.bot_fat;
                data.advice = getSeverelyOverweightAdvice(context, gender, isChild);
                break;
            default:
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
