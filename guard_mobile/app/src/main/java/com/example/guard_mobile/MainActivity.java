package com.example.guard_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //打开软件检测是否登录过，如果登录过，打开activity_main，否则打开activity_login


        super.onCreate(savedInstanceState);
        Log.d("MainActivity","打开软件检测是否登录过，如果登录过，打开activity_main，否则打开activity_login");

        SharedPreferences loginStatus=getSharedPreferences("sharedLoginStatus", Context.MODE_PRIVATE);
        if(loginStatus.getString("loginStatus", "").equals("Login")){
            setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        }else{
            setContentView(R.layout.activity_login);
            Button loginButton=findViewById(R.id.login);
            final EditText usernameEditText=findViewById(R.id.username);
            final EditText upasswordEditText=findViewById(R.id.password);




            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = usernameEditText.getText().toString().trim();
                    final String password = upasswordEditText.getText().toString().trim();

                    Log.i("TAG", username + "_" + password);

//                    //创建一个请求队列
//                    RequestQueue requestQueue= Volley.newRequestQueue(LogActivity.this);
//                    //创建一个请求
//                    //String url="https://www.baidu.com";
//                    String url="http://192.168.1.140:8010/guard/user/login";
//
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            //mEtusername.setText(s);
//                            Toast.makeText(LogActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
//                            Intent intent=new Intent(LogActivity.this,MainActivity.class);
//                            startActivity(intent);
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            mEtusername.setText(volleyError.toString());
//
//                            Toast.makeText(LogActivity.this, "登录请求失败！", Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String,String> map =new HashMap<String, String>();
//
//                            map.put("username",username);
//                            map.put("password",password);
//
//                            return map;
//                        }
//                    };
//
//                    // 设置Volley超时重试策略
//                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//
//
//                    //将创建的请求添加到请求队列中
//                    requestQueue.add(stringRequest);

                }
            });

        }


    }

}
