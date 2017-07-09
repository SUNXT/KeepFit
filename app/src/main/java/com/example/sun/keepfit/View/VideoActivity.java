package com.example.sun.keepfit.View;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.sun.keepfit.R;
import com.example.sun.keepfit.Util.DensityUtil;

import java.net.Inet4Address;

public class VideoActivity extends Activity implements GestureDetector.OnGestureListener,View.OnTouchListener{
    private VideoView videoView;

    private GestureDetector mDetector;
    private RelativeLayout root_layout;// 根布局
    /** 视频窗口的宽和高 */
    private int playerWidth, playerHeight;
    private boolean firstScroll = false;// 每次触摸屏幕后，第一次scroll的标志
    private float mBrightness = -1f; // 亮度
    private static final float STEP_VOLUME = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快
    private int GESTURE_FLAG = 0;// 1，调节音量,2.调节亮度
    private static final int GESTURE_MODIFY_VOLUME = 1;
    private static final int GESTURE_MODIFY_BRIGHT = 2;
    private AudioManager audiomanager;
    private int maxVolume, currentVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);
        videoView= (VideoView) findViewById(R.id.videoView);
        videoView.setMediaController(new MediaController(this));
        Uri videoUri = Uri.parse(Environment.getExternalStorageDirectory()
                .getPath() + "/DCIM/Camera/test.mp4");//视频路径
        videoView.setVideoURI(videoUri);
        videoView.start();
        /** 获取视频播放窗口的尺寸 */
        ViewTreeObserver viewObserver = root_layout.getViewTreeObserver();
        viewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                root_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                playerWidth = root_layout.getWidth();
                playerHeight = root_layout.getHeight();
            }
        });
        mDetector = new GestureDetector(this,this);
        mDetector.setIsLongpressEnabled(true);
        root_layout.setLongClickable(true);
        root_layout.setOnTouchListener(this);
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
        }
        return mDetector.onTouchEvent(event);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        bundle.putInt("currentPosition",videoView.getCurrentPosition());
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle){
        super.onRestoreInstanceState(bundle);
        Integer currentPosition=bundle.getInt("currentPosition");
        if(currentPosition!=null)
            videoView.seekTo(currentPosition);
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        firstScroll = true;// 设定是触摸屏幕后第一次scroll的标志
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float mOldX = e1.getX(), mOldY = e1.getY();
        int y = (int) e2.getRawY();
        if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
            if (mOldX > playerWidth * 3.0 / 5) {// 音量
                GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
            } else if (mOldX < playerWidth * 2.0 / 5) {// 亮度
                GESTURE_FLAG = GESTURE_MODIFY_BRIGHT;
            }
        }
        // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
        else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
            currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
            if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                if (distanceY >= DensityUtil.dip2px(this, STEP_VOLUME)) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                    if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                        currentVolume++;
                    }
                } else if (distanceY <= -DensityUtil.dip2px(this, STEP_VOLUME)) {// 音量调小
                    if (currentVolume > 0) {
                        currentVolume--;
                    }
                }
                audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume, 0);
            }
        }
        // 如果每次触摸屏幕后第一次scroll是调节亮度，那之后的scroll事件都处理亮度调节，直到离开屏幕执行下一次操作
        else if (GESTURE_FLAG == GESTURE_MODIFY_BRIGHT) {
            if (mBrightness < 0) {
                mBrightness = getWindow().getAttributes().screenBrightness;
                if (mBrightness <= 0.00f)
                    mBrightness = 0.50f;
                if (mBrightness < 0.01f)
                    mBrightness = 0.01f;
            }
            WindowManager.LayoutParams lpa = getWindow().getAttributes();
            lpa.screenBrightness = mBrightness + (mOldY - y) / playerHeight;
            if (lpa.screenBrightness > 1.0f)
                lpa.screenBrightness = 1.0f;
            else if (lpa.screenBrightness < 0.01f)
                lpa.screenBrightness = 0.01f;
            getWindow().setAttributes(lpa);
        }

        firstScroll = false;// 第一次scroll执行完成，修改标志
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
