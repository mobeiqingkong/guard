package com.example.myapplication.FallDetection;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Timer;

public class FallService extends Service {
    private static final String TAG = "!!!!!dujuke:Service!!!!";
    private float[] fallData=new float[3];
    public Fall fall;
    private boolean running = false;
    private final int FELL = 0;
    private DetectThread detectThread;
    private SensorHelper sensorHelper;
    private Vibrator vibrator;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private FallReceiver fallReceiver;

    private TextView countingView;
    private Dialog dialog;
    private Timer timer;
    private boolean isVibrate;
    private Context context;

    public FallService() {
    }

    /**
     * 绑定回调接口
     */
    public class MyBinder extends Binder {
        public FallService getService(){//创建获取Service的方法
            return FallService.this;//返回当前Service类
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnBind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorHelper=new SensorHelper(this);
        sensorHelper.initSensor();
        sensorHelper.regSensor();
        this.context = context;
        //vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);

        fall=new Fall();
        fall.setThresholdValue(23,5);
        running=true;
        //在通知栏上显示服务运行
        //showInNotification();
        Log.d(TAG, "FallService.onCreate()");
        detectThread = new DetectThread();
        detectThread.start();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.broadcast.FALL_LOCAL_BROADCAST");
        fallReceiver = new FallReceiver();
        localBroadcastManager.registerReceiver(fallReceiver, intentFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "FallDetectionService.onStartCommand");
        running=true;
        detectThread = new DetectThread();
        detectThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detectThread.interrupt();
        sensorHelper.unregSensor();
        localBroadcastManager.unregisterReceiver(fallReceiver);
        Log.d(TAG, "FallService.onDestroy()解绑服务打印结果");
    }



    //开一个线程用于检测跌倒
    class DetectThread extends Thread{
        @Override
        public void run() {
            super.run();
            fall.fallDetection();
            Log.d(TAG, "DetectThread.start()");
            while (running) {
                if (fall.isFell()) {
                    Log.e(TAG, "摔倒了!!!!!!!!!!!!!!!");

                    running = false;
                    Message msg = handler1.obtainMessage();
                    msg.what = FELL;
                    handler1.sendMessage(msg);
                    fall.setFell(false);
                    fall.cleanData();
                    stopSelf();
                    Log.e(TAG, "DetectThreadStopped！！！");
                }
            }

        }
    }



    private Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case FELL:
                    Log.e(TAG, "FELL");
                    //报警
                    //vibrator.vibrate(new long[]{5000,1000,5000,1000}, -1);
                    //showAlertDialog();
                    Intent intent = new Intent("com.broadcast.FALL_LOCAL_BROADCAST");
                    localBroadcastManager.sendBroadcast(intent);
                    break;
            }
        }
    };

}
