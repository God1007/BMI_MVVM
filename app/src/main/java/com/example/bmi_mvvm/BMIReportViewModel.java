package com.example.bmi_mvvm; // 指定包名，声明报告 ViewModel 的命名空间

import android.app.Application; // 导入 Application 以便获取应用级 Context

import androidx.annotation.NonNull; // 导入注解确保非空
import androidx.lifecycle.AndroidViewModel; // 导入 AndroidViewModel 以便持有 Application
import androidx.lifecycle.LiveData; // 导入 LiveData 用于暴露数据
import androidx.lifecycle.MutableLiveData; // 导入 MutableLiveData 支持更新

/**
 * 报告页面的 ViewModel，负责从模型读取上一次 BMI 结果并暴露给界面。
 */
public class BMIReportViewModel extends AndroidViewModel { // 继承 AndroidViewModel 以获取 Application

    private final BMIReportModel model; // 持有报告模型
    private final MutableLiveData<BMIReportData> reportData = new MutableLiveData<>(); // 保存报告数据的 LiveData

    public BMIReportViewModel(@NonNull Application application) { // 构造函数接收 Application
        super(application); // 调用父类构造
        model = new BMIReportModel(application); // 初始化模型
        loadReport(); // 构造时立即加载上次报告
    } // 构造函数结束

    public LiveData<BMIReportData> getReportData() { // 获取报告数据的 LiveData
        return reportData; // 返回可观察对象
    } // getReportData 结束

    public void loadReport() { // 从模型加载上次计算结果
        BMIReportData data = model.loadLastReport(getApplication()); // 调用模型读取数据
        reportData.setValue(data); // 更新 LiveData 供界面显示
    } // loadReport 结束

    public void saveReport(BMIReportData data) { // 将报告数据保存到模型
        model.saveReport(data); // 委托模型保存
        reportData.setValue(data); // 同步更新 LiveData
    } // saveReport 结束
} // BMIReportViewModel 类结束
