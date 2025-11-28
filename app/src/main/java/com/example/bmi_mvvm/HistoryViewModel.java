package com.example.bmi_mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * 为历史页面提供数据的 ViewModel，负责从仓库读取历史记录并放入 LiveData。
 */
public class HistoryViewModel extends AndroidViewModel {

    private final BMIHistoryRepository repository;
    private final MutableLiveData<List<BMIRecord>> history = new MutableLiveData<>();

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new BMIHistoryRepository(application.getApplicationContext());
        loadHistory();
    }

    public LiveData<List<BMIRecord>> getHistory() {
        return history;
    }

    /**
     * 读取数据库中的所有历史记录并立即推送给观察者。
     */
    public void loadHistory() {
        history.setValue(repository.getAllRecords());
    }
}
