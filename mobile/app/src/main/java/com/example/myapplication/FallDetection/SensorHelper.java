package com.example.myapplication.FallDetection;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

public class SensorHelper {
    private static final String TAG = "!!dujuke:SensorHelper!!";
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Context context;
    private float svm;
    private float mX, mY, mZ;
    public Fall fall;

    public SensorHelper(Context context){
        this.context = context;
        fall = new Fall();
    }

    public void initSensor(){
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (null == mSensorManager) {
            Log.d(TAG, "deveice not support SensorManager");
        }
    }


    public void regSensor() {
        mSensorManager.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_GAME);//SENSOR_DELAY_GAME
        Log.d(TAG, "注册成功");
    }

    public void unregSensor(){
        mSensorManager.unregisterListener(sensorEventListener);
        Log.d(TAG, "注销成功");
    }

    public SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //.setThresholdValue(25,5);
            //fall = new Fall();
            if (event.sensor == null) {
                Log.d(TAG, "event.sensor==null");
                return;
            }
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mX = event.values[0];
                mY = event.values[1];
                mZ = event.values[2];
                svm = (float) Math.sqrt(mX * mX + mY * mY + mZ * mZ);
                //svm=24;
                //Log.d(TAG, "X:" + mX + "  Y:" + mY + "  Z:" + mZ);
                Fall.svmCollector(svm);
                Fall.setSvmFilteringData();//中值滤波获取svm
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
