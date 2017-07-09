package com.example.sun.keepfit.View;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.sun.keepfit.Fragment.CommunityFragment;
import com.example.sun.keepfit.Fragment.MineFragment;
import com.example.sun.keepfit.Fragment.RunningFragment;
import com.example.sun.keepfit.Fragment.TrainFragment;
import com.example.sun.keepfit.MyFragment;
import com.example.sun.keepfit.R;

public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener{
    private TextView textView_sport;
    private TextView textView_train;
    private TextView textView_share;
    private TextView textView_mine;
    private RunningFragment runningFragment;
    private CommunityFragment communityFragment;
    private MineFragment myFragment_mine;
    private TrainFragment myFragment_train;
    private FragmentManager fManager;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//        startActivity(intent);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    /**
     * 初始化视图，将控件与变量绑定
     */
    public void initView(){
   /*   mViewPager = (ViewPager)findViewById(R.id.viewpage);
        mTabHomepage = (LinearLayout)findViewById(R.id.homepage);
        mTabSportShare = (LinearLayout)findViewById(R.id.sport_share);
        mTabMine = (LinearLayout)findViewById(R.id.mine_fragment_content);
        */
        textView_mine = (TextView) findViewById(R.id.tab_textView_mine);
        textView_share = (TextView) findViewById(R.id.tab_textView_share);
        textView_sport = (TextView) findViewById(R.id.tab_textView_sport);
        textView_train = (TextView) findViewById(R.id.tab_textView_train);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        /**
         * 将系统的状态栏设置为自己需要的颜色
         */
       // setStatusBackground();
    }
    /**
     * 设置状态栏颜色
     */
    private void setStatusBackground(){
        Window window = getWindow();
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        int statusColor = Resources.getSystem().getColor(R.color.theme_blue);
       //First translucent status bar.
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int statusBarHeight = 10;

        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
            //如果已经为 ChildView 设置过了 marginTop, 再次调用时直接跳过
            if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
                //不预留系统空间
                ViewCompat.setFitsSystemWindows(mChildView, false);
                lp.topMargin += statusBarHeight;
                mChildView.setLayoutParams(lp);
            }
        }

        View statusBarView = mContentView.getChildAt(0);
        if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
            //避免重复调用时多次添加 View
            statusBarView.setBackgroundColor(statusColor);
            return;
        }
        statusBarView = new View(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        statusBarView.setBackgroundColor(statusColor);
//向 ContentView 中添加假 View
        mContentView.addView(statusBarView, 0, lp);
    }

    /**
     * 初始化ViewPage
     */
    /**
    public void initViewPage(){
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        View view_homepage = mLayoutInflater.inflate(R.layout.homepage,null);
        View view_short_share = mLayoutInflater.inflate(R.layout.sport_share,null);
        View view_mine = mLayoutInflater.inflate(R.layout.mine_fragment_content,null);
        //向mView中添加View
        mViews.add(view_homepage);
        mViews.add(view_short_share);
        mViews.add(view_mine);

        mPagerAdapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViews.get(position));

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
    }
    */
    private void initEvent(){
        //将底部菜单绑定单击事件
        textView_sport.setOnClickListener(this);
        textView_train.setOnClickListener(this);
        textView_share.setOnClickListener(this);
        textView_mine.setOnClickListener(this);
        fManager = getSupportFragmentManager();//FragmentManager();
        setTextViewSelected(R.id.tab_textView_sport);
        runningFragment = new RunningFragment();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.add(R.id.frameLayout, runningFragment);
        fTransaction.commit();
    }
    /**
     * 下面是页面活动事件
     */
    @Override
    public void onClick(View arg0){
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        int id = arg0.getId();
        setTextViewSelected(id);
        switch (id){
            case R.id.tab_textView_sport:
                if(runningFragment == null){
                    runningFragment = new RunningFragment();
                    fTransaction.add(R.id.frameLayout, runningFragment);
                }
                else
                {
                    fTransaction.show(runningFragment);
                }
                break;
            case R.id.tab_textView_train:
                if(myFragment_train == null){
                    myFragment_train = new TrainFragment();
                    fTransaction.add(R.id.frameLayout, myFragment_train);
                }
                else
                {
                    fTransaction.show(myFragment_train);
                }
                break;
            case R.id.tab_textView_share:
                if(communityFragment == null){
                    communityFragment = new CommunityFragment();
                    fTransaction.add(R.id.frameLayout, communityFragment);
                }
                else
                {
                    fTransaction.show(communityFragment);
                }
                break;
            case R.id.tab_textView_mine:
                if(myFragment_mine == null){
                    myFragment_mine = new MineFragment();//MyFragment(R.layout.mine_fragment_content);
                    fTransaction.add(R.id.frameLayout,myFragment_mine);
                }
                else
                {
                    fTransaction.show(myFragment_mine);
                }
        }
        fTransaction.commit();
    }
    private  void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(runningFragment != null)fragmentTransaction.hide(runningFragment);
        if(myFragment_mine != null)fragmentTransaction.hide(myFragment_mine);
        if(communityFragment != null)fragmentTransaction.hide(communityFragment);
        if(myFragment_train!= null)fragmentTransaction.hide(myFragment_train);
    }
    /**
     * 根据id切换底部的图标和字体颜色
     */
    private void setTextViewSelected(int id){
        textView_sport.setSelected(false);
        textView_train.setSelected(false);
        textView_share.setSelected(false);
        textView_mine.setSelected(false);
        TextView textView = (TextView) findViewById(id);
        textView.setSelected(true);
    }
}
