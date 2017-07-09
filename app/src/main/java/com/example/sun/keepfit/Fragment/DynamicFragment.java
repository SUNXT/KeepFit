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
import android.widget.ListView;

import com.example.sun.keepfit.Adapter.ChoicenessListAdapter;
import com.example.sun.keepfit.Adapter.DynamicItemListAdapter;
import com.example.sun.keepfit.Adapter.ImagePaperAdapter;
import com.example.sun.keepfit.Bean.ChoicenessItem;
import com.example.sun.keepfit.Bean.DynamicItem;
import com.example.sun.keepfit.R;
import com.example.sun.keepfit.Util.ListViewUtil;
import com.example.sun.keepfit.View.CourseDetailActivity;
import com.example.sun.keepfit.View.DynamicDetailActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by SUN on 2016/12/24.
 */
public class DynamicFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    public final static int REFRESH_COMPLETE = 1;
    public final static int REFRESH_TIME_LONG = 500;
    private SwipeRefreshLayout swipeRefreshLayout;

//    private TextView textView_remind;//提示添加课程的按钮
//    private SwipeMenuListView showCourseListView;//显示已选课程的列表
    private ListView listView;
    private DynamicItemListAdapter listAdapter;
    private LinkedList<DynamicItem> mData;//保存当前课程的列表

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_COMPLETE://更新
                    getCoursesList();
                    ListViewUtil.setListViewHeightBasedOnChildren(listView);
                    listAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }

    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_dynamic_fragment_content,container,false);
        initView(view);
        bindingEvent();
        return view;
    }
    /**
     *
     */
    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshlayout_dynamic);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.theme_blue);

        listView = (ListView) view.findViewById(R.id.listView_show_dynamic);
        mData = new LinkedList<>();
//        getCoursesList();
        listAdapter = new DynamicItemListAdapter(getContext(),mData);
        listView.setAdapter(listAdapter);
//        ListViewUtil.setListViewHeightBasedOnChildren(listView);
    }

    /**
     * 为控件绑定事件
     */
    private void bindingEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }


    private void getCoursesList(){
        DynamicItem dynamicItem = new DynamicItem();
        dynamicItem.setUser_name("帅哥");
        dynamicItem.setDynamic_create_time("2017");
        dynamicItem.setDynamic_detail("2017年我要上精选！");
        for(int i = 0; i < 6; i++){
            mData.add(dynamicItem);
        }
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(REFRESH_COMPLETE, REFRESH_TIME_LONG);
    }
}
