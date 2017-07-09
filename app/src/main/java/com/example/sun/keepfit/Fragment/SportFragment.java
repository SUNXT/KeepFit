package com.example.sun.keepfit.Fragment;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sun.keepfit.R;
import com.example.sun.keepfit.Services.LocationRecorder;
import com.example.sun.keepfit.View.MapActivity;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import static com.amap.api.services.core.SearchUtils.getSHA1;

/**
 * Created by asus on 2016/12/14.
 */
public class SportFragment extends Fragment implements View.OnClickListener {
    private LocationRecorder recorder = null;
    private TextView speed;
    private TextView distance;
    private TextView time;
    private TextView consume;
    private Handler listener = null;
    private RelativeLayout start;
    private LinearLayout runningPanel;
    private ImageView restartOrStop,finish,map;
    private Boolean isStarted=true;
    private Boolean isGPSConnented=false;
    private Handler GPSChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if(message.getData().getBoolean("enabled")) {
                recorder.start();
                start.setVisibility(View.INVISIBLE);
                runningPanel.setVisibility(View.VISIBLE);
            }
            else {
                recorder.stop();
                start.setVisibility(View.VISIBLE);
                runningPanel.setVisibility(View.INVISIBLE);
                isGPSConnented=false;
            }
            toast.hide();
        }
    };

    private CustomToast toast;//消息显示框


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport, container, false);
        start = (RelativeLayout) view.findViewById(R.id.relativeLayout2);
        start.setOnClickListener(this);
        runningPanel= (LinearLayout) view.findViewById(R.id.runningPanel);
        speed = (TextView) view.findViewById(R.id.textView_speedOfRun);
        distance = (TextView) view.findViewById(R.id.textView_totalRunningMileage);
        time = (TextView) view.findViewById(R.id.textView_timeOfRun);
        consume= (TextView) view.findViewById(R.id.textView_powerOfRun);
        restartOrStop= (ImageView) view.findViewById(R.id.restartOrStop);
        restartOrStop.setOnClickListener(this);
        finish= (ImageView) view.findViewById(R.id.finish);
        finish.setOnClickListener(this);
        map= (ImageView) view.findViewById(R.id.map);
        map.setOnClickListener(this);
        recorder = LocationRecorder.getInstance(getActivity().getApplicationContext(), getActivity());
        listener = new Handler() {
            @Override
            public void handleMessage(Message message) {
                int state = message.getData().getInt("msg");
                if (state == LocationRecorder.UPDATE) {
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    distance.setText(decimalFormat.format(recorder.getDistance() / 1000));
                    time.setText(format((int)(recorder.getDuration() / 1000)));
                    speed.setText(""+(int)(recorder.getAverage()*3.6));
                    consume.setText(""+(int)(60*1.036*recorder.getDuration()/1000000));
                }
            }
        };
        recorder.addListener(listener);
        recorder.startLocation();
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.relativeLayout2:
                if(!isGPSConnented)
                    initGPS();
                else{
                    Bundle data = new Bundle();
                    data.putBoolean("enabled",true);
                    Message message = new Message();
                    message.setData(data);
                    GPSChangeHandler.sendMessage(message);
                }
                break;
            case R.id.restartOrStop:
                if(isStarted) {
                    isStarted=false;
                    restartOrStop.setImageResource(R.mipmap.start);
                    recorder.stop();
                }else{
                    isStarted=true;
                    restartOrStop.setImageResource(R.mipmap.stop);
                    recorder.start();
                }
                break;
            case R.id.map:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;
            case R.id.finish:
                time.setText("00:00:00");
                distance.setText("0.00");
                speed.setText("--");
                consume.setText("0");
                recorder.reset();
                start.setVisibility(View.VISIBLE);
                runningPanel.setVisibility(View.INVISIBLE);
                break;


        }
    }

    private void initGPS() {
        final LocationManager locationManager = (LocationManager) getActivity().getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        //加入监听器监听GPS开关事件
        ContentObserver mGpsMonitor = new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(!enabled) {
                    Bundle data = new Bundle();
                    data.putBoolean("enabled", enabled);
                    Message message = new Message();
                    message.setData(data);
                    GPSChangeHandler.sendMessage(message);
                }else
                    addGPSStatusListener();//监听GPS状态
            }
        };
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getActivity().getContentResolver()
                    .registerContentObserver(
                            Settings.Secure
                                    .getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
                            false, mGpsMonitor);
            //要求用户打开GPS
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("请打开GPS");
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                        }
                    });
            dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        }else
            addGPSStatusListener();//监听GPS状态
    }

    private void addGPSStatusListener() {
        //显示正在搜索GPS
        if(!isGPSConnented) {
            toast = new CustomToast(getActivity().getApplicationContext());
            toast.show("正在搜索GPS，请稍等", CustomToast.LENGTH_MAX);
        }
        final LocationManager locationManager = (LocationManager) getActivity().getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        GpsStatus.Listener statusListener = new GpsStatus.Listener() {
            public void onGpsStatusChanged(int event) {
                // TODO Auto-generated method stub
                GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                //Utils.DisplayToastShort(GPSService.this, "GPS status listener  ");
                switch (event) {
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        locationManager.removeGpsStatusListener(this);
                        Bundle data = new Bundle();
                        data.putBoolean("enabled",true);
                        Message message = new Message();
                        message.setData(data);
                        GPSChangeHandler.sendMessage(message);
                        isGPSConnented=true;
                        break;
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
        }else {
            locationManager.addGpsStatusListener(statusListener);//侦听GPS状态
        }
    }

    //自定义toast
    public class CustomToast {
        private int count=0;
        public static final int LENGTH_MAX = -1;
        private boolean mCanceled = true;
        private Handler mHandler;
        private Context mContext;
        private Toast mToast;

        public CustomToast(Context context) {
            this(context,new Handler());
        }

        public CustomToast(Context context,Handler h) {
            mContext = context;
            mHandler = h;
            mToast = Toast.makeText(mContext,"",Toast.LENGTH_SHORT);
        }

        public void show(int resId,int duration) {
            mToast.setText(resId);
            if(duration != LENGTH_MAX) {
                mToast.setDuration(duration);
                mToast.show();
            } else if(mCanceled) {
                mToast.setDuration(Toast.LENGTH_LONG);
                mCanceled = false;
                showUntilCancel();
            }
        }

        /**
         * @param text 要显示的内容
         * @param duration 显示的时间长
         * 根据LENGTH_MAX进行判断
         * 如果不匹配，进行系统显示
         * 如果匹配，永久显示，直到调用hide()
         */
        public void show(String text,int duration) {
            mToast.setText(text);
            if(duration != LENGTH_MAX) {
                mToast.setDuration(duration);
                mToast.show();
            } else {
                if(mCanceled) {
                    mToast.setDuration(Toast.LENGTH_LONG);
                    mCanceled = false;
                    showUntilCancel();
                }
            }
        }

        /**
         * 隐藏Toast
         */
        public void hide(){
            mToast.cancel();
            mCanceled = true;
            count=0;
        }

        public boolean isShowing() {
            return !mCanceled;
        }

        private void showUntilCancel() {
            if(mCanceled)
                return;
            mToast.show();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    if(count<10) {
                        count++;
                        showUntilCancel();
                    }
                    else {
                        show("搜索失败，请关闭GPS", Toast.LENGTH_LONG);
                        recorder.deactivate();
                        hide();
                    }
                }
            },3000);
        }
    }

    private String format(int s){
        int hour=0,minute=0,second=s;
        String result="";
        if(second>60){
            minute=second/60;
            second=second%60;
        }
        if(minute>60){
            hour=minute/60;
            minute=minute%60;
        }
        if(hour>9){
            result+=hour;
        }else if(hour>0){
            result+="0"+hour;
        }else {
            result+="00";
        }
        result+=":";
        if(minute>9){
            result+=hour;
        }else if(minute>0){
            result+="0"+hour;
        }else {
            result+="00";
        }
        result+=":";
        if(second>9){
            result+=second;
        }else if(second>0){
            result+="0"+second;
        }else {
            result+="00";
        }
        return result;
    }
}
