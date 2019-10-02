package com.example.myapplication.ui.userinfo;

import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;

public class UserinfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserinfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");

    }

    public androidx.lifecycle.LiveData<String> getText() {
        return mText;
    }
}