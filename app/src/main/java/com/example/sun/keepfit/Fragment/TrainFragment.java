package com.example.sun.keepfit.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.app.ActionBar.LayoutParams;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.sun.keepfit.Adapter.ImagePaperAdapter;
import com.example.sun.keepfit.Adapter.ListAdapter;
import com.example.sun.keepfit.Bean.Course;
import com.example.sun.keepfit.R;
import com.example.sun.keepfit.Util.ListViewUtil;
import com.example.sun.keepfit.View.AddTrainCourseActivity;
import com.example.sun.keepfit.View.CourseDetailActivity;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by SUN on 2016/12/24.
 */
public class TrainFragment extends Fragment implements View.OnClickListener{
    private TextView textView_remind;//提示添加课程的按钮
    private SwipeMenuListView showCourseListView;//显示已选课程的列表
    private ListAdapter listAdapter_course;
    private LinkedList<Course> mData;//保存当前课程的列表
    private ImageButton button_add_course;//添加课程

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
            if(msg.what == 100){
                mviewPager.setCurrentItem(currentItem);
            }
        }

    };

    /**
     *
     */
    private void initView(View view){
        textView_remind = (TextView) view.findViewById(R.id.textView_remind);
        mviewPager = (ViewPager) view.findViewById(R.id.viewpager);
        dotLayout = (LinearLayout) view.findViewById(R.id.doLayout);
        dotLayout.removeAllViews();
        showCourseListView = (SwipeMenuListView) view.findViewById(R.id.listView_show_choose_courses);
        button_add_course = (ImageButton) view.findViewById(R.id.imageButton_add_course);
//        button_add_course.setVisibility(View.INVISIBLE);
        initShowCourseListView();
    }
    private void initShowCourseListView(){
        mData = new LinkedList<>();
        listAdapter_course = new ListAdapter(getContext(),mData);
        showCourseListView.setAdapter(listAdapter_course);
        createSwipeMenu(showCourseListView);
    }
    private void createSwipeMenu(SwipeMenuListView mListView){
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 创建修改标题菜单项
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext());
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(120));
//                deleteItem.setTitleColor(R.color.white);
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitle("删除课程");
                deleteItem.setTitleSize(20);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        mListView.setMenuCreator(creator);
        // 设置为左滑
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        //设置按钮点击事件
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                final LinkedList<Course> linkedList = listAdapter_course.getmData();
                final String title = linkedList.get(position).getTitle();
                switch (index) {
                    case 0://删除课程
                        linkedList.remove(position);
                        listAdapter_course.setmData(linkedList);
                        listAdapter_course.notifyDataSetChanged();
                        if(listAdapter_course.getmData().size() == 0){
//                            button_add_course.setVisibility(View.INVISIBLE);
                            textView_remind.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return true;
            }
        });
        mListView.setFocusable(true);
        mListView.setFocusableInTouchMode(true);
        //列表项被点击时的事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
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

                params.leftMargin = 15;//设置小圆点的外边距
                params.rightMargin = 15;

                params.height = 20;//设置小圆点的大小
                params.width = 20;

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

            img1.setBackgroundResource(R.drawable.train_1);
            img2.setBackgroundResource(R.drawable.train_2);
            img3.setBackgroundResource(R.drawable.photo_1);
            img4.setBackgroundResource(R.drawable.photo_2);

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
            /**
             * 点击了提示文本，跳转到添加课程的界面
             */
            case R.id.textView_remind:
                Intent intent1 = new Intent(getActivity(), AddTrainCourseActivity.class);
                startActivity(intent1);
                break;
            /**
             * 点击了搜索图标，实现搜索添加课程功能
             */
            case R.id.imageButton_add_course:
                Intent intent = new Intent(getActivity(), AddTrainCourseActivity.class);
                startActivity(intent);
                break;
        }
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
        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
        startActivity(intent);
    }
    /**
     * 为控件绑定事件
     */
    private void bindEvent(){
        textView_remind.setOnClickListener(this);
        button_add_course.setOnClickListener(this);
        /**
         * 点击了训练的课程，跳转到训练的界面
         */
        showCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),"选中了"+i+"课程",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.train_fragment_content,container,false);
        initView(view);
        initViewPager(inflater);
        bindEvent();
        if(isAutoPlay){
            startPlay();
        }
        return view;
    }
}
