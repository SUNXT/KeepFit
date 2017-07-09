package com.example.sun.keepfit.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coder.circlebar.CircleBar;
import com.example.sun.keepfit.Database.KFDataBase;
import com.example.sun.keepfit.R;
import com.example.sun.keepfit.Services.StepDetector;
import com.example.sun.keepfit.Services.StepService;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

/**
 * Created by Administrator on 2016/12/4.
 */
public class pedometerFragment extends Fragment {

    private CircleBar progress;
    private BarChart Bchart;
    private TextView today_step;
    private TextView target_step;
    private int todayOffset;
    private KFDataBase dbHandler;
    private MyReceiver receiver=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHandler = new KFDataBase(getActivity());
        View view = inflater.inflate(R.layout.pedometer,container,false);
        Intent intent = new Intent(getActivity(), StepService.class);
        getActivity().startService(intent);
        today_step = (TextView) view.findViewById(R.id.step_count);
        target_step = (TextView) view.findViewById(R.id.target_step_count);
        Log.d("1", StepDetector.CURRENT_SETP+"");
//        target_step.setText(StepDetector.CURRENT_SETP);
        KFDataBase db = KFDataBase.getInstance(getActivity());
        int count = db.getCurrentSteps();
        today_step.setText(String.valueOf(count));
        progress = (CircleBar) view.findViewById(R.id.view);
        progress.setAnimationTime(3000);
        progress.setMaxstepnumber(Integer.parseInt(target_step.getText().toString()));
        progress.update(count,3000);
        Bchart = (BarChart) view.findViewById(R.id.view2);
        for(int i = 1;i<=7;i++){
            BarModel barModel = new BarModel(i + "日",0, Color.parseColor("#99CC00"));
            barModel.setColor(Color.parseColor("#13b6f6"));
            barModel.setValue(i);
            Bchart.addBar(barModel);
        }
        Bchart.update();

        //注册广播接收器
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.sun.keepfit.StepsService");
        getActivity().registerReceiver(receiver,filter);
        return view;
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            int count=bundle.getInt("steps");
            Log.d("test1", "onReceive: "+today_step.getText().toString()+""+count);
            today_step.setText(String .valueOf(count));
            progress.setMaxstepnumber(Integer.parseInt(target_step.getText().toString()));
            progress.update(count,3000);
        }
    }

}
