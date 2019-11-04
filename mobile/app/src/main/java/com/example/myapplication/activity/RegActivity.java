package com.example.myapplication.activity;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegActivity extends AppCompatActivity {

    private EditText mEtusername,mEttel,mEtemail,mEtname, mEtpwd1, mEtpwd2, mEtcodes;
    private Button mBtnregin, mBtnsend;



    public int num;
///////////////////////////////////////////生成验证码函数////////////////////////////////////////////
    private int generateWord() {
        String[] beforeShuffle = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        int result = Integer.parseInt(afterShuffle.substring(1, 5));
        return result;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        findViews();

/////////////////////////////////////////按下注册按钮////////////////////////////////////////////////
        mBtnregin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String username = mEtusername.getText().toString().trim();
                final String tel=mEttel.getText().toString().trim();
                final String email=mEtemail.getText().toString().trim();
                final String name=mEtname.getText().toString().trim();
                String pwd1 = mEtpwd1.getText().toString().trim();
                final String pwd2 = mEtpwd2.getText().toString().trim();
                String Code = mEtcodes.getText().toString().trim();




                Log.i("TAG",username+"_"+tel+"_"+email+"_"+name+"_"+pwd1+"_"+pwd2+"_"+Code);


                if (username.isEmpty()|| tel.isEmpty()|| email.isEmpty() || name.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty())
                    Toast.makeText(RegActivity.this, "请完整填写注册信息！", Toast.LENGTH_SHORT).show();
                else {
                    if (Code.isEmpty())
                        Toast.makeText(RegActivity.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
                    else if (Integer.parseInt(Code) != num)
                        Toast.makeText(RegActivity.this, "验证码错误！", Toast.LENGTH_SHORT).show();
                    else {
                        if (!pwd1.equals(pwd2)) {

                            Toast.makeText(RegActivity.this, "两次输入密码不同，请重新输入！", Toast.LENGTH_SHORT).show();
                       } else{




                            //创建一个请求队列
                            final RequestQueue requestQueue=Volley.newRequestQueue(RegActivity.this);
                            //创建一个请求
                            //String url="https://www.baidu.com";
                            String url="http://192.168.31.20:8010/guard/user/add";

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    //mEtusername.setText(s);
                                    Toast.makeText(RegActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(RegActivity.this,LogActivity.class);
                                    startActivity(intent);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    //mEtusername.setText(volleyError.toString());
                                    Toast.makeText(RegActivity.this, "注册请求失败！", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> map =new HashMap<String, String>();

                                    map.put("username",username);
                                    map.put("tel",tel);
                                    map.put("email",email);
                                    map.put("name",name);
                                    map.put("password",pwd2);

                                    return map;
                                }
                            };
                            requestQueue.add(stringRequest);
                        }
                    }
                }
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////




//////////////////////////////////////////发送验证码/////////////////////////////////////////////////
        mBtnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = generateWord();
                AlertDialog.Builder builder = new AlertDialog.Builder(RegActivity.this);
                builder.setTitle("请记住验证码");//设置对话框的标题
                builder.setMessage(String.valueOf(num));//设置对话框的内容
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(RegActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog b = builder.create();
                b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
                Log.i("tag1", String.valueOf(num));
            }
        });
    }
////////////////////////////////////////////////////////////////////////////////////////////////////



    private void findViews() {
        mEtusername = findViewById(R.id.et_username);
        mEttel = findViewById(R.id.et_tel);
        mEtemail = findViewById(R.id.et_email);
        mEtname = findViewById(R.id.et_name);
        mEtpwd1 = findViewById(R.id.et_pwd1);
        mEtpwd2 = findViewById(R.id.et_pwd2);
        mEtcodes = findViewById(R.id.et_Codes);
        mBtnsend = findViewById(R.id.btn_send);
        mBtnregin = findViewById(R.id.btn_regin);
    }
}
