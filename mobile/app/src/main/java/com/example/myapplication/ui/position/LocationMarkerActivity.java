package com.example.myapplication.ui.position;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.example.myapplication.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * AMapV2地图中介绍自定义可旋转的定位图标
 */
public class LocationMarkerActivity extends Activity implements LocationSource,
        AMapLocationListener {
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;

	public TextView txtlocinfo;
	private TextView mLocationErrText;
	private static final int STROKE_COLOR = Color.argb(120, 3, 145, 255);
	private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
	private boolean mFirstFix = false;
	private Marker mLocMarker;
	private SensorEventHelper mSensorHelper;
	private Circle mCircle;
	public static final String LOCATION_MARKER_FLAG = "mylocation";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏

		setContentView(R.layout.locationsource_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		Button btreturn = (Button)findViewById(R.id.btn_return);
		btreturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	/**
	 * 初始化
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();  //获取地图
			setUpMap();
		}
		mSensorHelper = new SensorEventHelper(this);
		if (mSensorHelper != null) {
			mSensorHelper.registerSensorListener();
		}
		mLocationErrText = (TextView)findViewById(R.id.location_errInfo_text);
		mLocationErrText.setVisibility(View.GONE);
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true来显示定位层并可触发定位
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {

			super.onResume();
			mapView.onResume();
			if (mSensorHelper != null) {
				mSensorHelper.registerSensorListener();
			}


	}


	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (mSensorHelper != null) {
			mSensorHelper.unRegisterSensorListener();
			mSensorHelper.setCurrentMarker(null);
			mSensorHelper = null;
		}
		mapView.onPause();
		deactivate();
		mFirstFix = false;
	}
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		if(null != mlocationClient){
			mlocationClient.onDestroy();
		}
	}
	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		StringBuffer sb = new StringBuffer();
		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		if (mListener != null && amapLocation != null)
		{
			if (amapLocation != null && amapLocation.getErrorCode() == 0)
			{
				sb.append("地    址: " + amapLocation.getProvince() + "\n");
				sb.append(amapLocation.getCity() + "\n");
				sb.append(amapLocation.getDistrict() + "\n");
				sb.append(amapLocation.getStreet() + "\n");
				sb.append(amapLocation.getStreetNum() + "\n");
				sb.append(amapLocation.getPoiName() + "\n");
				String result = sb.toString();
				bundle.putString("locinfo",result);
				mLocationErrText.setVisibility(View.GONE);
				LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
				if (!mFirstFix) {
					mFirstFix = true;
					addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
					addMarker(location,result);//添加定位图标
					mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
				} else {
					mCircle.setCenter(location);
					mCircle.setRadius(amapLocation.getAccuracy());
					mLocMarker.setPosition(location);
				}
				aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
				bundle.putString("locinfo",errText);
				mLocationErrText.setVisibility(View.VISIBLE);
				mLocationErrText.setText(errText);
			}
		}
		intent.putExtras(bundle);
		setResult(0x11,intent);

	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mLocationOption.setNeedAddress(true);
			mlocationClient.setLocationOption(mLocationOption);
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}
	private void addCircle(LatLng latlng, double radius) {
		CircleOptions options = new CircleOptions();
		options.strokeWidth(1f);
		options.fillColor(FILL_COLOR);
		options.strokeColor(STROKE_COLOR);
		options.center(latlng);
		options.radius(radius);
		mCircle = aMap.addCircle(options);
	}

	private void addMarker(LatLng latlng, String string) {
		if (mLocMarker != null) {
			return;
		}
		Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.navi_map_gps_locked);
		BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
//		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
		MarkerOptions options = new MarkerOptions();
		options.icon(des);
		options.anchor(0.5f, 0.5f);
		options.position(latlng);
		options.snippet(string);
		options.draggable(true);
		mLocMarker = aMap.addMarker(options);
		mLocMarker.showInfoWindow();
		mLocMarker.setTitle(LOCATION_MARKER_FLAG);
	}
	
}
