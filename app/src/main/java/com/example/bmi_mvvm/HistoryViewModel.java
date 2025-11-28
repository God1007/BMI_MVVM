package com.example.bmi_mvvm; // 指定包名，声明历史 ViewModel 所在命名空间

import androidx.lifecycle.LiveData; // 导入 LiveData 以便界面观察
import androidx.lifecycle.MutableLiveData; // 导入 MutableLiveData 支持更新
import androidx.lifecycle.ViewModel; // 导入 ViewModel 基类

import java.util.List; // 导入 List 接口用于持有记录

/**
 * 历史页面的 ViewModel，负责从仓库读取 BMI 历史并暴露给界面。
 */
public class HistoryViewModel extends ViewModel { // 定义 HistoryViewModel 类

    private final BMIHistoryRepository repository = new BMIHistoryRepository(App.getContext()); // 仓库实例，使用全局 Context
    private final MutableLiveData<List<BMIRecord>> history = new MutableLiveData<>(); // LiveData 保存历史记录列表

    public HistoryViewModel() { // 构造函数
        loadHistory(); // 创建时立即加载历史数据
    } // 构造函数结束

    public LiveData<List<BMIRecord>> getHistory() { // 暴露历史数据的 LiveData
        return history; // 返回可观察对象
    } // getHistory 结束

    public void loadHistory() { // 从仓库加载历史记录
        history.setValue(repository.getHistory()); // 将查询结果填充到 LiveData
    } // loadHistory 结束
} // HistoryViewModel 类结束
