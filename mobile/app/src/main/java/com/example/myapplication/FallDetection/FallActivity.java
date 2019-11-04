package com.example.myapplication.FallDetection;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class FallActivity extends AppCompatActivity {

    private Switch mswitch;
    private static final String TAG = "!!dujuke:FallActivity!!";
    private ServiceConnection conn;
    private boolean switchsituation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall);
        mswitch = findViewById(R.id.switch1);
        final SharedPreferences sharedPreferences=getSharedPreferences("User",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        switchsituation=sharedPreferences.getBoolean("Switch",false);
        if(switchsituation)
        {
            mswitch.setChecked(true);
            bindMyService();
        }else {
            mswitch.setChecked(false);
        }
        //监听switch，如果是打开开关，则绑定服务，否则解绑服务
        mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mswitch.isChecked())
                {
                    bindMyService();
                    editor.putBoolean("Switch",true);
                    editor.apply();
                    Toast.makeText(FallActivity.this,"start service",Toast.LENGTH_SHORT).show();
                }
                else{
                    unbindMyService();
                    editor.putBoolean("Switch",false);
                    editor.apply();
                    Toast.makeText(FallActivity.this,"stop service",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    //绑定FallService
    public void bindMyService(){
        Intent intent=new Intent(this, FallService.class);
        if(conn==null){
            conn=new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            bindService(intent,conn,BIND_AUTO_CREATE);
            Toast.makeText(this,"bind Service",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"已经bindService",Toast.LENGTH_SHORT).show();
        }
    }
    //解绑
    public void unbindMyService(){
        Intent intent=new Intent(this, FallService.class);
        if(conn!=null){
            stopService(intent);
            unbindService(conn);
            Log.e(TAG,"unbind Service");
            Toast.makeText(this,"unbind Service",Toast.LENGTH_SHORT).show();
            conn=null;
        }else {
            Toast.makeText(this,"还未bindService",Toast.LENGTH_SHORT).show();
        }
    }

    //在Activity死亡之前调用
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(conn!=null){
            conn=null;
        }
    }

}
