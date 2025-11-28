package com.example.bmi_mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

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

    public void loadHistory() {
        history.setValue(repository.getAllRecords());
    }
}
