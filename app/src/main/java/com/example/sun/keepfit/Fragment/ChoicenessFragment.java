package com.example.sun.keepfit.Fragment;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.sun.keepfit.Adapter.ChoicenessListAdapter;
import com.example.sun.keepfit.Adapter.ImagePaperAdapter;
import com.example.sun.keepfit.Adapter.ListAdapter;
import com.example.sun.keepfit.Bean.ChoicenessItem;
import com.example.sun.keepfit.Bean.Course;
import com.example.sun.keepfit.R;
import com.example.sun.keepfit.Util.ListViewUtil;
import com.example.sun.keepfit.View.CourseDetailActivity;
import com.example.sun.keepfit.View.DynamicDetailActivity;
import com.example.sun.keepfit.View.Main2Activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by SUN on 2016/12/24.
 */
public class ChoicenessFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    public final static int REFRESH_COMPLETE = 1;
    public final static int REFRESH_TIME_LONG = 500;

    private RelativeLayout r1,r2,r3;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新

//    private TextView textView_remind;//提示添加课程的按钮
//    private SwipeMenuListView showCourseListView;//显示已选课程的列表
    private GridView gridView;
    private ChoicenessListAdapter listAdapter;
    private LinkedList<ChoicenessItem> mData;//保存当前课程的列表
//    private ImageButton button_add_course;//添加课程

    private ViewPager mviewPager;
    /**用于小圆点图片*/
    private List<ImageView> dotViewList;
    /**用于存放轮播效果图片*/
    private List<ImageView> list;

    LinearLayout dotLayout;

    private int currentItem  = 0;//当前页面

    boolean isAutoPlay = true;//是否自动轮播

    private ScheduledExecutorService scheduledExecutorService;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    mviewPager.setCurrentItem(currentItem);
                    break;
                case REFRESH_COMPLETE://更新
                    getCoursesList();
                    ListViewUtil.setGridViewHeightBasedOnChildren(gridView,2);
                    listAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_choiceness_fragment_content,container,false);
        initView(view);
        initViewPager(inflater);
        bindingEvent();
        if(isAutoPlay){
            startPlay();
        }
        Log.d("ChoicenessFragment","调用了");
        return view;
    }
    /**
     *
     */
    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.theme_blue);
//        textView_remind = (TextView) view.findViewById(R.id.textView_remind);
        mviewPager = (ViewPager) view.findViewById(R.id.viewpager_choiceness);
        dotLayout = (LinearLayout) view.findViewById(R.id.doLayout_choinceness);
        dotLayout.removeAllViews();
        gridView = (GridView) view.findViewById(R.id.gridView_show_choiceness);
        mData = new LinkedList<>();
        getCoursesList();
        listAdapter = new ChoicenessListAdapter(getContext(),mData);
        gridView.setAdapter(listAdapter);
        ListViewUtil.setGridViewHeightBasedOnChildren(gridView,2);
        System.out.println("列数1："+gridView.getNumColumns());

        r1 = (RelativeLayout) view.findViewById(R.id.abc);
        r2 = (RelativeLayout) view.findViewById(R.id.abc2);
        r3 = (RelativeLayout) view.findViewById(R.id.abc1);

        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
    }

    /**
     * 为控件绑定事件
     */
    private void bindingEvent(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),DynamicDetailActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * 初始化广告轮播视图
     * @param inflater
     */
    public void initViewPager(LayoutInflater inflater){
        dotViewList = new ArrayList<ImageView>();
        list = new ArrayList<ImageView>();

        try {
            for (int i = 0; i < 4; i++) {
                ImageView dotView = new ImageView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                params.leftMargin = 12;//设置小圆点的外边距
                params.rightMargin = 12;

                params.height = 15;//设置小圆点的大小
                params.width = 15;

                if (i == 0) {
                    dotView.setBackgroundResource(R.drawable.point_pressed);
                } else {

                    dotView.setBackgroundResource(R.drawable.point_unpressed);
                }
                dotLayout.addView(dotView, params);

                dotViewList.add(dotView);
                //上面是动态添加了四个小圆点
            }


            ImageView img1 = new ImageView(getActivity());// (ImageView) inflater.inflate(R.layout.scroll_view_item, null);
            ImageView img2 = (ImageView) inflater.inflate(R.layout.scroll_view_item, null);
            ImageView img3 = (ImageView) inflater.inflate(R.layout.scroll_view_item, null);
            ImageView img4 = (ImageView) inflater.inflate(R.layout.scroll_view_item, null);

            img1.setBackgroundResource(R.drawable.p1);
            img2.setBackgroundResource(R.drawable.p2);
            img3.setBackgroundResource(R.drawable.p3);
            img4.setBackgroundResource(R.drawable.p4);

            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoDetail(0);
                }
            });
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoDetail(0);
                }
            });
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoDetail(0);
                }
            });
            img4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoDetail(0);
                }
            });

            list.add(img1);
            list.add(img2);
            list.add(img3);
            list.add(img4);

            ImagePaperAdapter adapter = new ImagePaperAdapter((ArrayList) list);

            mviewPager.setAdapter(adapter);
            mviewPager.setCurrentItem(0);
            mviewPager.setOnPageChangeListener(new MyPageChangeListener());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 10, 4, TimeUnit.SECONDS);
        //根据他的参数说明，第一个参数是执行的任务，第二个参数是第一次执行的间隔，第三个参数是执行任务的周期；
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.abc:
            case R.id.abc1:
            case R.id.abc2:
                Intent intent = new Intent(getActivity(), Main2Activity.class);
                startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(REFRESH_COMPLETE, REFRESH_TIME_LONG);

    }

    /**
     *执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (mviewPager) {
                currentItem = (currentItem+1)%list.size();
                handler.sendEmptyMessage(100);
            }
        }
    }
    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     *
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isAutoPlay = false;
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    System.out.println(" 手势滑动，空闲中");
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    System.out.println(" 界面切换中");
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (mviewPager.getCurrentItem() == mviewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        mviewPager.setCurrentItem(0);
                        System.out.println(" 滑动到最后一张");
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (mviewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        mviewPager.setCurrentItem(mviewPager.getAdapter().getCount() - 1);
                        System.out.println(" 滑动到第一张");
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub
            //这里面动态改变小圆点的被背景，来实现效果
            currentItem = pos;
            for(int i=0;i < dotViewList.size();i++){
                if(i == pos){
                    ((View)dotViewList.get(pos)).setBackgroundResource(R.drawable.point_pressed);
                }else {
                    ((View)dotViewList.get(i)).setBackgroundResource(R.drawable.point_unpressed);
                }
            }
        }

    }

    /**
     * 跳转到详细页面
     * @param id id为课程的id
     */
    private void gotoDetail(int id){
        Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
        startActivity(intent);
    }

    private void getCoursesList(){
        ChoicenessItem choicenessItem = new ChoicenessItem("2017年第一组训练，我要上推选！这一年我要减肥。","123","1232",null);
        for(int i = 0; i < 4; i++){
            mData.add(choicenessItem);
        }
    }

}
