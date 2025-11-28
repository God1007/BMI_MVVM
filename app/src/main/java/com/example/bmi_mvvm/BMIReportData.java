package com.example.bmi_mvvm;


/**
 * 报告页面需要展示的组合数据对象，包含图片资源和文字建议。
 * 由 BMIReportModel 计算后返回给 ViewModel 使用。
 */
public class BMIReportData {
    // 图片资源 ID，比如 R.drawable.bot_fit
    public int imageRes;

    // 建议文字
    public String advice;

    // 可选：你也可以加构造函数
    public BMIReportData() {}

    public BMIReportData(int imageRes, String advice) {
        this.imageRes = imageRes;
        this.advice = advice;
    }
}
