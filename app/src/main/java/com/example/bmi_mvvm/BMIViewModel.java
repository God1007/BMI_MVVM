package com.example.bmi_mvvm;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 主页面的 ViewModel，负责 BMI 计算、数据校验、持久化以及历史记录写入。
 * 通过 LiveData 将结果与错误信息推送给 UI 层。
 */
public class BMIViewModel extends ViewModel {

    private BMIModel model;
    private BMIHistoryRepository historyRepository;

    private MutableLiveData<String> bmi = new MutableLiveData<>();
    private MutableLiveData<String> category = new MutableLiveData<>();
    private MutableLiveData<String> height = new MutableLiveData<>();
    private MutableLiveData<String> weight = new MutableLiveData<>();
    private MutableLiveData<String> age = new MutableLiveData<>();
    private MutableLiveData<String> gender = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public BMIViewModel() {
        model = new BMIModel(App.getContext()); // App.getContext() 或者传入 Context
        historyRepository = new BMIHistoryRepository(App.getContext());
    }

    public LiveData<String> getBmi() { return bmi; }
    public LiveData<String> getCategory() { return category; }
    public LiveData<String> getHeight() { return height; }
    public LiveData<String> getWeight() { return weight; }
    public LiveData<String> getAge() { return age; }
    public LiveData<String> getGender() { return gender; }
    public LiveData<String> getError() { return error; }

    /**
     * 将当前输入同步保存到本地，用户离开页面再回来时可继续编辑。
     */
    public void persistInputs(String h, String w, String a, String g) {
        model.saveData(h, w, a, g);
    }

    /**
     * 负责校验输入、执行 BMI 计算、分类判定，并保存结果与历史。
     * 通过 LiveData 依次推送分类、身高、体重等字段，确保观察者拿到完整数据。
     */
    public void calculateBMI(String h, String w, String a, String g, Context context) {
        if (h.isEmpty() || w.isEmpty() || a.isEmpty()) {
            error.setValue(context.getString(R.string.error_empty_fields));
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
            model.saveResult(String.format("%.2f", bmiVal), categoryStr);
            historyRepository.saveTodayResult(bmiVal);

            // 先更新除 bmi 以外的字段，确保 bmi 观察者触发时数据完整
            category.setValue(categoryStr);
            height.setValue(h);
            weight.setValue(w);
            age.setValue(a);
            gender.setValue(g);
            bmi.setValue(String.format("%.2f", bmiVal));

        } catch (NumberFormatException e) {
            error.setValue(context.getString(R.string.error_invalid_number));
            e.printStackTrace();
        }
    }

    /**
     * 加载上一次保存的输入数据，用于初始化界面。
     */
    public String[] loadSavedData() {
        return model.loadData();
    }
}
