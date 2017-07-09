package com.example.sun.keepfit.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.example.sun.keepfit.R;

import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final Intent intent;
        intent = new Intent(this,LoginActivity.class);
        intent.putExtra("sendinfo","welcome");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                finish();
                startActivity(intent); //执行
            }
        };
        timer.schedule(task, 1000 * 2); //2秒后
    }
}
