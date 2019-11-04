package com.example.myapplication.ui.position;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PositionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PositionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public androidx.lifecycle.LiveData<String> getText() {
        return mText;
    }
}