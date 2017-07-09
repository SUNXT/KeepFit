package com.example.sun.keepfit.View;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.example.sun.keepfit.R;
import com.example.sun.keepfit.Services.LocationRecorder;

import java.util.List;

/**
 * Created by asus on 2016/12/17.
 */
public class MapActivity extends Activity{
    private MapView mMapView = null;
    private AMap amap=null;
    private PolylineOptions mPolyoptions;
    private Polyline mpolyline;
    private Handler listener;
    private LocationRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basicmap_activity);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        init();
        initpolyline();
    }

    private void init(){
        if (amap == null) {
            amap = mMapView.getMap();
        }
        recorder=LocationRecorder.getInstance();
        amap.setLocationSource(recorder);// 设置定位监听
        amap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        amap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        amap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        listener=new Handler(){
            @Override
            public void handleMessage(Message message){
                int state=message.getData().getInt("msg");
                switch (state){
                    case LocationRecorder.STOP:
                        initpolyline();
                        break;
                    case LocationRecorder.UPDATE:
                        AMapLocation aMapLocation=recorder.getNewLocation();
                        LatLng mylocation = new LatLng(aMapLocation.getLatitude(),
                                aMapLocation.getLongitude());
                        mPolyoptions.add(mylocation);
                        redrawline();
                        break;
                }
            }
        };
        recorder.addListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        initpolyline();
        mMapView.onResume();
        List<List<LatLng>> latLngList=recorder.getLatLngList();
        for(int i=0;i<latLngList.size();++i){
            mPolyoptions.addAll(latLngList.get(i));
            amap.addPolyline(mPolyoptions);
            initpolyline();
        }
        mPolyoptions.addAll(recorder.getCurrentLatLng());
        mpolyline=amap.addPolyline(mPolyoptions);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    private void initpolyline() {
        mPolyoptions = new PolylineOptions();
        mPolyoptions.width(10f);
        mPolyoptions.color(Color.BLUE);
        mpolyline=null;
    }

    private void redrawline() {
        if (mPolyoptions.getPoints().size() > 1) {
            if (mpolyline != null) {
                mpolyline.setPoints(mPolyoptions.getPoints());
            } else {
                mpolyline = amap.addPolyline(mPolyoptions);
            }
        }
    }
}
