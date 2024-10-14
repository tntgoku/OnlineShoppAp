package com.example.onlineshopp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityModel extends ViewModel {
    private final MutableLiveData<String> data = new MutableLiveData<>();

    public void setData(String value) {
        data.setValue(value);
    }

    public LiveData<String> getData() {
        return data;
    }
}
