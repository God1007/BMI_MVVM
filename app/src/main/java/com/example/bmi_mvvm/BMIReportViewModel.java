package com.example.bmi_mvvm;

import android.content.Context;
import android.content.Intent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 负责报告页的数据准备，接收 Intent 中的参数，转交给模型计算，
 * 再通过 LiveData 暴露给界面层。
 */
public class BMIReportViewModel extends ViewModel {

    private final MutableLiveData<String> bmi = new MutableLiveData<>();
    private final MutableLiveData<String> category = new MutableLiveData<>();
    private final MutableLiveData<String> details = new MutableLiveData<>();
    private final MutableLiveData<String> advice = new MutableLiveData<>();
    private final MutableLiveData<Integer> imageRes = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final BMIReportModel model = new BMIReportModel();

    public LiveData<String> getBmi() { return bmi; }
    public LiveData<String> getCategory() { return category; }
    public LiveData<String> getDetails() { return details; }
    public LiveData<String> getAdvice() { return advice; }
    public LiveData<Integer> getImageRes() { return imageRes; }
    public LiveData<String> getError() { return error; }

    /**
     * 从 Intent 读取 BMI 相关参数，调用模型获取报告数据，
     * 然后将格式化后的文本与图片资源更新到各个 LiveData。
     */
    public void loadReportData(Intent intent, Context context) {
        try {
            String bmiValue = intent.getStringExtra("bmi");
            String bmiCategory = intent.getStringExtra("bmi_category");
            String height = intent.getStringExtra("height");
            String weight = intent.getStringExtra("weight");
            String age = intent.getStringExtra("age");
            String gender = intent.getStringExtra("gender");

            int ageVal = Integer.parseInt(age);

            // 使用 Model 获取图片 + 建议
            BMIReportData data = model.getBMIReportData(context, bmiCategory, gender, ageVal);

            // 更新 LiveData
            bmi.setValue(bmiValue);
            category.setValue(bmiCategory);
            details.setValue(context.getString(R.string.details_format, height, weight, age, gender));
            imageRes.setValue(data.imageRes);
            advice.setValue(data.advice);

        } catch (Exception e) {
            // 捕获任何异常并向界面层抛出友好提示，避免闪退。
            error.setValue(context.getString(R.string.report_load_failed));
            e.printStackTrace();
        }
    }

}
