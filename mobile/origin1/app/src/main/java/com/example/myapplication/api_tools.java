package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class api_tools {
    public void login(final Activity activity, final String username, final String password) {
        //创建一个请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        //创建一个请求
        String url = "http://192.168.1.140:8010/guard/user/login";

        final SharedPreferences sharedPreferences = activity.getSharedPreferences("User", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                //获取json内部数据//
                String json = s;
                JSONObject user = JSON.parseObject(json);
                String deleted = user.getString("deleted");
                String relative = user.getString("relative");
                String enable = user.getString("enable");
                String _id = user.getString("_id");
                String name = user.getString("name");
                String email = user.getString("email");
                String password = user.getString("password");
                String username = user.getString("username");
                String tel = user.getString("tel");
                String __v = user.getString("__v");

                editor.putString("deleted", deleted);
                editor.putString("relative", relative);
                editor.putString("enable", enable);
                editor.putString("_id", _id);
                editor.putString("name", name);
                editor.putString("email", email);
                editor.putString("password", password);
                editor.putString("username", username);
                editor.putString("tel", tel);
                editor.putString("__v", __v);
                editor.apply();
                if (sharedPreferences.getString("name", "") != "") {
                    Toast.makeText(activity, "登录成功！", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(activity, "登录请求失败,请检查用户名和密码是否正确！", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();

                map.put("username", username);
                map.put("password", password);

                Log.d("map是：", String.valueOf(map));
                return map;
            }
        };

        // 设置Volley超时重试策略
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) {
                Toast.makeText(activity, "登录请求失败,请检查用户名和密码是否正确！", Toast.LENGTH_LONG).show();
            }
        });
        //将创建的请求添加到请求队列中
        requestQueue.add(stringRequest);
    }




        public void updateUser(final Activity activity, final Map<String,String> newUserMap) {
        //创建一个请求队列
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        final SharedPreferences sharedPreferences = activity.getSharedPreferences("User", MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
        //创建一个请求
        String url="http://192.168.1.140:8010/guard/user/update";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject user = JSON.parseObject(s);
                if (user.getIntValue("nModified")==1) {


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //mEtusername.setText(volleyError.toString());
                Toast.makeText(activity, "修改失败,请检查链接是否正确！", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return newUserMap;
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
