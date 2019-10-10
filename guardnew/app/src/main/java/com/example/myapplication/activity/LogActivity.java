package com.example.myapplication.activity;

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

import java.util.HashMap;
import java.util.Map;


public class LogActivity extends AppCompatActivity {


    private Button mBtnlogin;
    private TextView mTvgotoreg;
    private EditText mEtusername,mEtpwd;
    private CheckBox mCbautologin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        findViews();



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
                    Log.i("TAG", username + "_" + password);

                    //创建一个请求队列
                    RequestQueue requestQueue= Volley.newRequestQueue(LogActivity.this);
                    //创建一个请求
                    String url="http://192.168.1.140:8011/guard/user/login";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            //获取json内部数据//
                            String json=s;
                            JSONObject user= JSON.parseObject(json);
                            String deleted=user.getString("deleted");
                            String relative=user.getString("relative");
                            String enable=user.getString("enable");
                            String _id=user.getString("_id");
                            String name=user.getString("name");
                            String email=user.getString("email");
                            String password=user.getString("password");
                            String username=user.getString("username");
                            String tel=user.getString("tel");
                            String __v=user.getString("__v");

                            editor.putString("deleted",deleted);
                            editor.putString("relative",relative);
                            editor.putString("enable",enable);
                            editor.putString("_id",_id);
                            editor.putString("name",name);
                            editor.putString("email",email);
                            editor.putString("password",password);
                            editor.putString("username",username);
                            editor.putString("tel",tel);
                            editor.putString("__v",__v);
                            editor.commit();


                            Toast.makeText(LogActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(LogActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //mEtusername.setText(volleyError.toString());
                            Toast.makeText(LogActivity.this, "登录请求失败,请检查用户名和密码是否正确！", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map =new HashMap<String, String>();

                            map.put("username",username);
                            map.put("password",password);

                            return map;
                        }
                    };

                    // 设置Volley超时重试策略
                    stringRequest.setRetryPolicy(new RetryPolicy(){
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });

                    //将创建的请求添加到请求队列中
                    requestQueue.add(stringRequest);
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
