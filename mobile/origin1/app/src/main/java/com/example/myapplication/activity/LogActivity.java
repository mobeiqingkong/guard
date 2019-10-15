package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.api_tools;

import java.util.HashMap;
import java.util.Map;


public class LogActivity extends AppCompatActivity {


    private Button mBtnlogin;
    private TextView mTvgotoreg;
    private EditText mEtusername,mEtpwd;
    private CheckBox mCbautologin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences sharedPreferences=getSharedPreferences("User",MODE_PRIVATE);
        if(sharedPreferences.getString("name","")!=""){
            Intent intent=new Intent(LogActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        findViews();


        //测试用登录
        Button btn_loginTest=findViewById(R.id.btn_loginTest);
        btn_loginTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=getSharedPreferences("User",MODE_PRIVATE);
                SharedPreferences.Editor editor=sh.edit();
                editor.putString("name","小白");
                editor.apply();
                Intent intent=new Intent(LogActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mBtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //////获取Sharedpreference对象//////
                final SharedPreferences sharedPreferences=getSharedPreferences("User",MODE_PRIVATE);
                /////////获取editor对象////////////
                final SharedPreferences.Editor editor=sharedPreferences.edit();


                final String username = mEtusername.getText().toString().trim();
                final String password = mEtpwd.getText().toString().trim();

                /*if(mCbrememberpwd.isChecked()){
                    editor.putString("username",username);
                    editor.putString("autopwd",password);
                    editor.commit();
                }

                if(!sharedPreferences.getString("username","").isEmpty() && !sharedPreferences.getString("password","").isEmpty())
                {
                    mEtusername.setText(sharedPreferences.getString("username",""));
                    mEtpwd.setText(sharedPreferences.getString("autopwd",""));
                    //自动登录
                }*/

                if(username.isEmpty()||password.isEmpty())
                    Toast.makeText(LogActivity.this, "请完整输入登录信息！", Toast.LENGTH_LONG).show();
                else{
                    api_tools apiTools=new api_tools();
                    apiTools.login(LogActivity.this,username,password);
                }



            }
        });


        mTvgotoreg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Intent intent = new Intent(LogActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViews() {
        mBtnlogin = findViewById(R.id.btn_login);
        mTvgotoreg = findViewById(R.id.tv_gotoreg);
        mTvgotoreg.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        mEtusername = findViewById(R.id.et_username);
        mEtpwd = findViewById(R.id.et_pwd);
        mCbautologin=findViewById(R.id.cb_autologin);
    }



}
