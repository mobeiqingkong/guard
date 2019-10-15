package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mTvuserinfo;


    private void findView(){
        mTvuserinfo=findViewById(R.id.tv_userinfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();

        final SharedPreferences sharedPreferences=getSharedPreferences("User",MODE_PRIVATE);

        mTvuserinfo.setText("username:"+sharedPreferences.getString("username","")+
                "name:"+sharedPreferences.getString("name","")+
                "tel:"+sharedPreferences.getString("tel",""));

    }
}
