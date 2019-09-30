package com.example.guard_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;

import java.lang.reflect.Array;

public class Preferences {
    private Context context;
    public Preferences(Context context)
    {
        this.context=context;
    }
    //保存用户设置偏好
    public void saveloginStatus(Object loginStatus)
    {
        //保存文件名字为"shared",保存形式为Context.MODE_PRIVATE即该数据只能被本应用读取
        SharedPreferences preferences=context.getSharedPreferences("sharedLoginStatus",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("sharedLoginStatus", loginStatus.toString());
        editor.putString("loginStatus", "Login");
        editor.apply();//提交数据
    }
    public void cleanloginStatus(){
        SharedPreferences shared=context.getSharedPreferences("sharedLoginStatus",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=shared.edit();
        editor.clear();
        editor.apply();//清除登录信息
    }

}