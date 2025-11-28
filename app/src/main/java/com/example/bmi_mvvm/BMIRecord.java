package com.example.bmi_mvvm;

/**
 * 用于封装一条 BMI 历史记录的简单数据类。
 * 包含记录日期、BMI 数值以及最后更新时间戳，便于图表与列表展示。
 */
public class BMIRecord {
    private final String date;
    private final float bmi;
    private final long updatedAt;

    public BMIRecord(String date, float bmi, long updatedAt) {
        this.date = date;
        this.bmi = bmi;
        this.updatedAt = updatedAt;
    }

    /**
     * 返回记录对应的日期字符串（yyyy-MM-dd）。
     */
    public String getDate() {
        return date;
    }

    /**
     * 返回当日计算得到的 BMI 数值。
     */
    public float getBmi() {
        return bmi;
    }

    /**
     * 返回写入数据库时的时间戳，便于排序或后续扩展。
     */
    public long getUpdatedAt() {
        return updatedAt;
    }
}
