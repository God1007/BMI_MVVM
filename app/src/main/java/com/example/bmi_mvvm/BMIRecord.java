package com.example.bmi_mvvm;

public class BMIRecord {
    private final String date;
    private final float bmi;
    private final long updatedAt;

    public BMIRecord(String date, float bmi, long updatedAt) {
        this.date = date;
        this.bmi = bmi;
        this.updatedAt = updatedAt;
    }

    public String getDate() {
        return date;
    }

    public float getBmi() {
        return bmi;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
}
