package com.example.sun.keepfit;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.test.suitebuilder.annotation.Suppress;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SUN on 2016/12/1.
 */
@SuppressLint("ValidFragment")
public class MyFragment extends android.support.v4.app.Fragment {
    private int layout_id;
    public MyFragment(){}
    public MyFragment(int id){
        super();
        layout_id = id;
        System.out.println("1---"+layout_id);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("2---"+layout_id);
        View view = inflater.inflate(layout_id,container,false);
        /*
        TextView textView = (TextView) view.findViewById(R.id.textView_totalRunningMileage);
        AssetManager mgr=getActivity().getAssets();//得到AssetManager
        Typeface tf= Typeface.createFromAsset(mgr, "fonts/myfont.ttf");//根据路径得到Typeface
        textView.setTypeface(tf);//设置字体
        */
        return view;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }
}
