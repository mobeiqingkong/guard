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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
            final EditText upasswordEditText=findViewById(R.id.et_login_pwd);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = usernameEditText.getText().toString().trim();
                    final String password = upasswordEditText.getText().toString().trim();
                    Log.i("登录username是", username);
                    Log.i("登录password是", password);

                    String Url = getString(R.string.guardApiIp)+getString(R.string.loginApi);
                    Log.i("接口是：", Url);
                    // 启动线程来执行任务
                    new Thread() {
                        public void run() {
                            //请求网络
                            try {
                                Login(username,password);
                            } catch (IOException | JSONException e) {
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

    public Boolean Login(String account, String passWord) throws IOException, JSONException {
        try {
            String httpUrl="http://cdz.ittun.cn/cdz/user_login.php";
            URL url = new URL(httpUrl);//创建一个URL
            HttpURLConnection connection  = (HttpURLConnection)url.openConnection();//通过该url获得与服务器的连接
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");//设置请求方式为post
            connection.setConnectTimeout(3000);//设置超时为3秒
            //设置传送类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            //提交数据
            String data = "&passwd=" + URLEncoder.encode(passWord, "UTF-8")+ "&number=" + URLEncoder.encode(account, "UTF-8")+ "&cardid=";//传递的数据
            connection.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
//            ToastUtils.showShort(this,
//                    "数据提交成功......");
            //获取输出流
            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            //获取响应输入流对象
            InputStreamReader is = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(is);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            //读取服务器返回信息
            while ((line = bufferedReader.readLine()) != null){
                strBuffer.append(line);
            }
            result = strBuffer.toString();
            is.close();
            connection.disconnect();
        } catch (Exception e) {
            return true;
        }
        return false;
    }



}
