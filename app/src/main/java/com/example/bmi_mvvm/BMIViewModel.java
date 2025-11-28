package com.example.bmi_mvvm; // 指定包名，确保类在应用命名空间内

import android.content.Context; // 导入 Context 以便获取字符串资源
import androidx.lifecycle.LiveData; // 导入 LiveData 用于可观察数据
import androidx.lifecycle.MutableLiveData; // 导入 MutableLiveData 支持更新数据
import androidx.lifecycle.ViewModel; // 导入 ViewModel 作为生命周期感知的数据持有者

/**
 * 主页面的 ViewModel，负责 BMI 计算、数据校验、持久化以及历史记录写入。
 * 通过 LiveData 将结果与错误信息推送给 UI 层。
 */
public class BMIViewModel extends ViewModel { // 定义 BMIViewModel 继承 ViewModel

    private BMIModel model; // 持有业务模型用于计算与持久化
    private BMIHistoryRepository historyRepository; // 持有历史记录仓库用于写入今天的结果

    private MutableLiveData<String> bmi = new MutableLiveData<>(); // LiveData：最新 BMI 数值
    private MutableLiveData<String> category = new MutableLiveData<>(); // LiveData：最新 BMI 分类
    private MutableLiveData<String> height = new MutableLiveData<>(); // LiveData：用户输入的身高
    private MutableLiveData<String> weight = new MutableLiveData<>(); // LiveData：用户输入的体重
    private MutableLiveData<String> age = new MutableLiveData<>(); // LiveData：用户输入的年龄
    private MutableLiveData<String> gender = new MutableLiveData<>(); // LiveData：用户选择的性别
    private MutableLiveData<String> error = new MutableLiveData<>(); // LiveData：错误提示文本

    public BMIViewModel() { // 构造函数，初始化模型与仓库
        model = new BMIModel(App.getContext()); // 创建模型，使用应用级 Context 避免泄漏
        historyRepository = new BMIHistoryRepository(App.getContext()); // 创建历史仓库，用于保存每日 BMI
    } // 构造函数结束

    public LiveData<String> getBmi() { return bmi; } // 提供 BMI LiveData 供界面观察
    public LiveData<String> getCategory() { return category; } // 提供分类 LiveData
    public LiveData<String> getHeight() { return height; } // 提供身高 LiveData
    public LiveData<String> getWeight() { return weight; } // 提供体重 LiveData
    public LiveData<String> getAge() { return age; } // 提供年龄 LiveData
    public LiveData<String> getGender() { return gender; } // 提供性别 LiveData
    public LiveData<String> getError() { return error; } // 提供错误提示 LiveData

    /**
     * 将当前输入同步保存到本地，用户离开页面再回来时可继续编辑。
     */
    public void persistInputs(String h, String w, String a, String g) { // 持久化输入的方法
        model.saveData(h, w, a, g); // 委托模型保存身高、体重、年龄与性别
    } // persistInputs 结束

    /**
     * 负责校验输入、执行 BMI 计算、分类判定，并保存结果与历史。
     * 通过 LiveData 依次推送分类、身高、体重等字段，确保观察者拿到完整数据。
     */
    public void calculateBMI(String h, String w, String a, String g, Context context) { // BMI 计算入口
        if (h.isEmpty() || w.isEmpty() || a.isEmpty()) { // 若任何输入为空
            error.setValue(context.getString(R.string.error_empty_fields)); // 发布错误提示
            return; // 直接返回不再计算
        } // 空值校验结束

        try { // 捕获可能的数字解析异常
            double heightVal = Double.parseDouble(h); // 将身高字符串转为 double
            double weightVal = Double.parseDouble(w); // 将体重字符串转为 double
            int ageVal = Integer.parseInt(a); // 将年龄字符串转为 int

            double bmiVal = model.calculateBMI(heightVal, weightVal); // 调用模型计算 BMI

            String categoryStr; // 保存计算出的分类文本
            if (ageVal >= 18) { // 成年人使用成人分类
                categoryStr = model.getAdultBMICategory(bmiVal, context); // 获取成人分类描述
            } else { // 未成年人使用儿童分类
                categoryStr = model.getChildBMICategory(bmiVal, ageVal, g, context); // 获取儿童分类描述
            } // 分类判断结束

            model.saveData(h, w, a, g); // 将用户输入持久化
            model.saveResult(String.format("%.2f", bmiVal), categoryStr); // 保存最新 BMI 数值与分类
            historyRepository.saveTodayResult(bmiVal); // 将数值写入当天历史记录

            category.setValue(categoryStr); // 更新分类 LiveData
            height.setValue(h); // 更新身高 LiveData
            weight.setValue(w); // 更新体重 LiveData
            age.setValue(a); // 更新年龄 LiveData
            gender.setValue(g); // 更新性别 LiveData
            bmi.setValue(String.format("%.2f", bmiVal)); // 更新 BMI LiveData，触发页面跳转

        } catch (NumberFormatException e) { // 捕获数字格式异常
            error.setValue(context.getString(R.string.error_invalid_number)); // 推送无效数字提示
            e.printStackTrace(); // 打印堆栈方便调试
        } // try-catch 结束
    } // calculateBMI 结束

    /**
     * 加载上一次保存的输入数据，用于初始化界面。
     */
    public String[] loadSavedData() { // 读取缓存输入的方法
        return model.loadData(); // 直接委托模型获取字符串数组
    } // loadSavedData 结束
} // BMIViewModel 类结束
