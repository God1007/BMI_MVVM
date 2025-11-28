package com.example.bmi_mvvm;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BMIViewModel extends ViewModel {

    private BMIModel model;

    private MutableLiveData<String> bmi = new MutableLiveData<>();
    private MutableLiveData<String> category = new MutableLiveData<>();
    private MutableLiveData<String> height = new MutableLiveData<>();
    private MutableLiveData<String> weight = new MutableLiveData<>();
    private MutableLiveData<String> age = new MutableLiveData<>();
    private MutableLiveData<String> gender = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public BMIViewModel() {
        model = new BMIModel(App.getContext()); // App.getContext() 或者传入 Context
    }

    public LiveData<String> getBmi() { return bmi; }
    public LiveData<String> getCategory() { return category; }
    public LiveData<String> getHeight() { return height; }
    public LiveData<String> getWeight() { return weight; }
    public LiveData<String> getAge() { return age; }
    public LiveData<String> getGender() { return gender; }
    public LiveData<String> getError() { return error; }

    public void calculateBMI(String h, String w, String a, String g, Context context) {
        if (h.isEmpty() || w.isEmpty() || a.isEmpty()) {
            error.setValue("请填写完整信息");
            return;
        }

        try {
            double heightVal = Double.parseDouble(h);
            double weightVal = Double.parseDouble(w);
            int ageVal = Integer.parseInt(a);

            double bmiVal = model.calculateBMI(heightVal, weightVal);

            String categoryStr;
            if (ageVal >= 18) {
                categoryStr = model.getAdultBMICategory(bmiVal, context);
            } else {
                categoryStr = model.getChildBMICategory(bmiVal, ageVal, g, context);
            }

            // 保存数据
            model.saveData(h, w, a, g);

            // 更新 LiveData
            bmi.setValue(String.format("%.2f", bmiVal));
            category.setValue(categoryStr);
            height.setValue(h);
            weight.setValue(w);
            age.setValue(a);
            gender.setValue(g);

        } catch (NumberFormatException e) {
            error.setValue("输入数据格式错误");
            e.printStackTrace();
        }
    }

    public String[] loadSavedData() {
        return model.loadData();
    }
}
