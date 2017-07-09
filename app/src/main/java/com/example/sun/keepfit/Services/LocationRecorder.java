package com.example.sun.keepfit.Services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2016/12/14.
 */
public class LocationRecorder implements AMapLocationListener,LocationSource {

    private static LocationRecorder recorder;

    private OnLocationChangedListener mListener;
    private List<List<AMapLocation>> recordList = new ArrayList<>();//当前轨迹（点集合）
    private List<AMapLocation> currentRecord=new ArrayList<>();//轨迹列表
    private List<List<LatLng>> latLngList = new ArrayList<>();
    private List<LatLng> currentLatLng=new ArrayList<>();
    private List<Map<String,Long>> timeList=new ArrayList<>();
    private Long currentStartTime;
    private Long currentEndTime;
    private boolean start=false;
    private float distance=0;
    private static final int MY_PERMISSIONS_REQUEST=1;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private Context context;
    private Activity activity;
    private List<Handler> handlers=new ArrayList<>();

    public static final int UPDATE=1;
    public static final int RESET=2;
    public static final int START=3;
    public static final int STOP=4;

    public float getDuration() {
        Long duration=0L;
        for(Map<String,Long> map:timeList){
            duration=duration+map.get("endTime")-map.get("startTime");
        }
        if(start)
            duration=duration+System.currentTimeMillis()-currentStartTime;
        return duration;
    }

    public float getAverage() {
        return getDistance()/getDuration()*1000;
    }

    public float getDistance() {
        if(currentRecord.size()>1) {
            AMapLocation firstpoint = currentRecord.get(currentRecord.size()-2);
            AMapLocation secondpoint = currentRecord.get(currentRecord.size()-1);
            LatLng firstLatLng = new LatLng(firstpoint.getLatitude(),
                    firstpoint.getLongitude());
            LatLng secondLatLng = new LatLng(secondpoint.getLatitude(),
                    secondpoint.getLongitude());
            double betweenDis = AMapUtils.calculateLineDistance(firstLatLng, secondLatLng);
            distance = (float) (distance + betweenDis);
        }
        return distance;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.v("------","locating");
        if (aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                if (start) {
                    currentRecord.add(aMapLocation);
                    currentLatLng.add(new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude()));
                    sendMessage(UPDATE);
                }
                if(mListener!=null)
                    mListener.onLocationChanged(aMapLocation);
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": "
                        + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
    }

    public void startLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);
        }else{
            if (mLocationClient == null) {
                mLocationClient = new AMapLocationClient(context);
                mLocationOption = new AMapLocationClientOption();
                // 设置定位监听
                mLocationClient.setLocationListener(this);
                // 设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mLocationOption.setInterval(1000);
                // 设置定位参数
                mLocationClient.setLocationOption(mLocationOption);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                mLocationClient.startLocation();
            }
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    public static LocationRecorder getInstance(Context context,Activity activity){
        if (recorder==null) {
            recorder = new LocationRecorder();
        }
        recorder.setActivity(activity);
        recorder.setContext(context);
        return recorder;
    }

    public static LocationRecorder getInstance(){return recorder;}

    public void setContext(Context context){
        this.context=context;
    }

    public void setActivity(Activity activity){
        this.activity=activity;
    }

    public void start(){
        if(!start) {
            start = true;
            currentStartTime = System.currentTimeMillis();
            sendMessage(START);
        }
    }

    public void stop(){
        if (start) {
            start = false;
            currentEndTime = System.currentTimeMillis();
            recordList.add(currentRecord);
            currentRecord = new ArrayList<>();
            latLngList.add(currentLatLng);
            currentLatLng=new ArrayList<>();
            Map<String, Long> map = new HashMap<>();
            map.put("startTime", currentStartTime);
            map.put("endTime", currentEndTime);
            timeList.add(map);
            sendMessage(STOP);
        }
    }

    private void sendMessage(int state){
        for(Handler handler:handlers){
            Message message=new Message();
            Bundle data=new Bundle();
            data.putInt("msg",state);
            message.setData(data);
            handler.sendMessage(message);
        }
    }

    public void addListener(Handler handler){
        handlers.add(handler);
    }

    public void reset(){
        stop();
        sendMessage(RESET);
        recordList.clear();
        currentRecord.clear();
        timeList.clear();
        latLngList.clear();
        currentLatLng.clear();
        distance=0;
    }

    public AMapLocation getNewLocation(){
        if(currentRecord.size()>0)
            return currentRecord.get(currentRecord.size()-1);
        else
            return null;
    }

    public List<AMapLocation> getCurrentRecord(){return currentRecord;}

    public List<LatLng> getCurrentLatLng(){return currentLatLng;}

    public List<List<AMapLocation>> getRecordList(){return recordList;}

    public List<List<LatLng>> getLatLngList(){return latLngList;}
}
