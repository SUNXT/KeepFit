package com.example.sun.keepfit.Fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.sun.keepfit.Adapter.MyFragmentPagerAdapter;
import com.example.sun.keepfit.MyFragment;
import com.example.sun.keepfit.R;

import java.util.ArrayList;


/**
 * Created by asus on 2016/12/4.
 */
public class RunningFragment extends android.support.v4.app.Fragment implements ViewPager.OnPageChangeListener{
    private View tag;
    private ViewPager pager;
    private ArrayList<Fragment> listViews;
    private int distance = 0;//移动图片的位移量
    private int bmpWidth;// 移动条图片的长度
    private ImageView cursor;

    public RunningFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.running_fragment_top,container,false);
        tag=view.findViewById(R.id.linearLayout);
        pager= (ViewPager) view.findViewById(R.id.pager);
        cursor= (ImageView) view.findViewById(R.id.cursor);
        tag.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cursor.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int range=tag.getWidth();
                bmpWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.line).getWidth();
                distance=range-bmpWidth;
            }
        });
        //加载Fragment
        listViews=new ArrayList<>();
        SportFragment f = new SportFragment();//(R.layout.running_fragment_content_1);
        listViews.add(f);
        pedometerFragment f1 =new pedometerFragment();
        listViews.add(f1);
        pager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(),listViews));
        pager.addOnPageChangeListener(this);
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Animation animation = null;
        switch (position) {
            case 0:
                animation=new TranslateAnimation(distance,0,0,0);
                break;
            case 1:
                animation=new TranslateAnimation(0,distance,0,0);
                break;
        }
        animation.setFillAfter(true);// true表示图片停在动画结束位置
        animation.setDuration(300); //设置动画时间为300毫秒
        cursor.startAnimation(animation);//开始动画
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
