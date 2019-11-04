package com.example.myapplication.FallDetection;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.myapplication.R;
import com.example.myapplication.ui.home.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class FallReceiver extends BroadcastReceiver implements AMapLocationListener {

    private TextView countingView;
    private AlertDialog dialog;
    private Timer timer;
    private SharedPreferences sharedPreferences;
    private Vibrator vibrator;
    private boolean isVibrate;
    private Context context;
    private MediaPlayer mediaPlayer;
    private AudioUtil audioUtil;

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationClientOption;
    public String locationAddress;
    public String locationTime;
    private Message message;
    private static final String TAG = "!!dujuke:FallReceiver!!";

    public FallReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        audioUtil=new AudioUtil(context.getApplicationContext());

        //audioUtil.setMediaVolume(20);//设置音量

        showAlertDialog();

        sharedPreferences=context.getSharedPreferences("User",MODE_PRIVATE);
        isVibrate = sharedPreferences.getBoolean("pre_key_vibrate", true);
        if(isVibrate){
            startVibrate();
        }
        startAlarm();
        startLocation();
    }

    private void showAlertDialog(){
        countingView = new TextView(context);
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //    设置Title的图标
        builder.setIcon(R.drawable.ic_warning);
        //    设置Title的内容
        builder.setTitle("摔倒警报");

        builder.setView(countingView);
        //    设置Content来显示一个信息
        builder.setMessage("检测到摔倒倒发生，10秒后发出警报");
        //    设置一个NegativeButton
        builder.setNegativeButton("取消报警", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                stopAlarm();
                timer.cancel();
                dialog.dismiss();
                if(isVibrate){
                    stopVibrate();
                }
                Intent startIntent = new Intent(context, FallService.class);
                context.startService(startIntent);
                //Toast.makeText(FallService.this, "已取消报警", Toast.LENGTH_SHORT).show();
            }
        });
        //builder.show();
        dialog = builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
        }else {
            dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        }
        dialog.setCanceledOnTouchOutside(false);
        countDown();
        dialog.show();
    }

    /*
    倒计时
     */
    private void countDown() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int countTime = 12;
            @Override
            public void run() {
                if(countTime > 0){
                    countTime --;
                }
                Message msgTime = handler.obtainMessage();
                msgTime.arg1 = countTime;
                handler.sendMessage(msgTime);
            }
        };
        timer.schedule(timerTask, 50, 1000);
    }


    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1 > 0){
                //动态显示倒计时
                msg.arg1=msg.arg1-1;
                countingView.setText("                         "
                        + msg.arg1 + "秒后自动报警");
            }else{
                //倒计时结束自动关闭
                if(dialog != null){
                    //dialog.dismiss();
                    if(isVibrate){
                        stopVibrate();
                    }
                    stopAlarm();
                    sendSMS(locationAddress, locationTime);
                    Log.e(TAG,"send  +  1");
                }
                timer.cancel();
            }
        }
    };

    /*
    开始震动
     */
    private void startVibrate(){
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 500, 100, 500};
        vibrator.vibrate(pattern, -1);
    }
    /*
    停止震动
     */
    private void stopVibrate(){
        vibrator.cancel();
    }

    /*
    开始播放铃声
     */
    private void startAlarm(){
        mediaPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.didi);
        mediaPlayer.setLooping(true);//设置循环
        mediaPlayer.start();
    }
    /*
    停止播放铃声
     */
    private void stopAlarm(){
        mediaPlayer.stop();
    }


    private void sendSMS(String address, String time){
        //获取短信管理器
        SmsManager smsManager = SmsManager.getDefault();

        String name = sharedPreferences.getString("name", " ");
        String phoneNum = sharedPreferences.getString("tel", " ");

        String smsContent = time + name + "在" + address + "发生摔倒了！";
        Log.e(TAG,name+"   "+phoneNum);
        Log.e(TAG,smsContent);
        if(phoneNum.trim().equals("")){
            Toast.makeText(context, "请设置短信号码后重试", Toast.LENGTH_SHORT).show();
        }else{
            smsManager.sendTextMessage(phoneNum, null, smsContent ,null, null);
            Toast.makeText(context, "短信已经发出", Toast.LENGTH_SHORT).show();
        }

    }

    private void startLocation(){
        Log.d(TAG, "FallLocalReceiver.startLocation()");
        locationClient = new AMapLocationClient(context);
        //初始化定位参数
        locationClientOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        locationClientOption.setOnceLocationLatest(true);

        //设置定位监听
        locationClient.setLocationListener(this);
        //启动定位
        locationClient.startLocation();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                locationAddress = aMapLocation.getAddress();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                locationTime = df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
}
