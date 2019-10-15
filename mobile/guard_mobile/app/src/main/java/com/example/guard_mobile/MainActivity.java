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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
public class MainActivity extends AppCompatActivity {
    String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //打开软件检测是否登录过，如果登录过，打开activity_main，否则打开activity_login


        super.onCreate(savedInstanceState);
        Log.d("MainActivity","打开软件检测是否登录过，如果登录过，打开activity_main，否则打开activity_login");

        SharedPreferences loginStatus=getSharedPreferences("sharedLoginStatus", Context.MODE_PRIVATE);
        if(loginStatus.getString("loginStatus", "").equals("Login")){
//            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//            startActivity(intent);
            setContentView(R.layout.activity_main);
//
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        }else{
            Log.d("","打印一下");
            setContentView(R.layout.activity_main_login);


            Button loginButton=findViewById(R.id.bt_login_submit);
            final EditText usernameEditText=findViewById(R.id.et_login_username);
            final EditText passwordEditText=findViewById(R.id.et_login_pwd);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = usernameEditText.getText().toString().trim();
                    final String password = passwordEditText.getText().toString().trim();
                    Log.i("登录username是", username);
                    Log.i("登录password是", password);

                    String Url = getString(R.string.guardApiIp)+getString(R.string.loginApi);
                    Log.i("接口是：", Url);
                    // 启动线程来执行任务
                    new Thread() {
                        public void run() {
                            //请求网络
                            try {


                                api_tools apiTools=new api_tools();
                                apiTools.login(MainActivity.this,username,password);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    //提示读取结果
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                                String isSuccess="false";
                                try {
                                    JSONObject resultJson=new JSONObject (result);
                                    isSuccess=resultJson.getString("isSuccess");
                                    Log.i("isSuccess是", isSuccess);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (!isSuccess.equals("true")) {
                                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
//                                    ToastUtils.showShort(LoginActivity.this, "密码错误......");
                                } else {
                                    final Intent it = new Intent(MainActivity.this, HomeActivity.class); //你要转向的Activity
                                    Timer timer = new Timer();
                                    TimerTask task = new TimerTask() {
                                        @Override
                                        public void run() {
                                            startActivity(it); //执行
                                        }
                                    };
                                    timer.schedule(task, 1000); //1秒后
                                }
                }
            });
        }
    }



}
