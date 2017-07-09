package com.example.sun.keepfit.Fragment;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.TextView;

import com.example.sun.keepfit.Adapter.MyFragmentPagerAdapter;
import com.example.sun.keepfit.MyFragment;
import com.example.sun.keepfit.R;

import java.util.ArrayList;

/**
 * Created by asus on 2016/12/4.
 */
public class CommunityFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private View tag;
    private ViewPager pager;
    private TextView pickup;
    private TextView action;
    private ArrayList<Fragment> listViews;
    private int offset=0;//图片偏移量
    private int distance = 0;//移动图片的位移量
    private int bmpWidth;// 移动条图片的长度
    private ImageView cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.comnunity,container,false);
        pager= (ViewPager) view.findViewById(R.id.pager2);
        tag=view.findViewById(R.id.tag);
        pickup= (TextView) view.findViewById(R.id.pickup);
        pickup.setTextColor(getResources().getColor(R.color.theme_blue));
        action= (TextView) view.findViewById(R.id.action);
        cursor= (ImageView) view.findViewById(R.id.cursor2);
        //监听tag加载，当加载完毕时，计算图片偏移量及位移量
        tag.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cursor.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int range=tag.getWidth();
                bmpWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.line).getWidth();
                offset=(range/2-bmpWidth)/2;
                Matrix matrix = new Matrix();
                matrix.postTranslate(offset, 0);
                cursor.setImageMatrix(matrix);// 设置动画初始位置
                distance=2*offset+bmpWidth;
            }
        });
        listViews=new ArrayList<>();
        ChoicenessFragment f=new ChoicenessFragment();
        listViews.add(f);
        DynamicFragment f1 = new DynamicFragment();
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
                pickup.setTextColor(getResources().getColor(R.color.theme_blue));
                action.setTextColor(getResources().getColor(R.color.theme_gray));
                break;
            case 1:
                animation=new TranslateAnimation(offset,distance,0,0);
                pickup.setTextColor(getResources().getColor(R.color.theme_gray));
                action.setTextColor(getResources().getColor(R.color.theme_blue));
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
