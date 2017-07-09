package com.example.sun.keepfit.Fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.sun.keepfit.Adapter.DynamicItemListAdapter;
import com.example.sun.keepfit.Bean.DynamicItem;
import com.example.sun.keepfit.R;
import com.example.sun.keepfit.Util.ListViewUtil;
import com.example.sun.keepfit.View.Main2Activity;
import com.example.sun.keepfit.View.Setting;

import java.util.LinkedList;

/**
 * Created by SUN on 2016/12/24.
 */
public class MineFragment extends Fragment implements View.OnClickListener{
//    private TextView textView_remind;//提示添加课程的按钮
//    private SwipeMenuListView showCourseListView;//显示已选课程的列表
//    private ListView listView;
//    private DynamicItemListAdapter listAdapter;
//    private LinkedList<DynamicItem> mData;//保存当前课程的列表

    private ImageButton imageButton_setting;
    private RelativeLayout r1,r2,r3,r4,r5,r6,r7;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment_content,container,false);
        initView(view);
        bindingEvent();
        return view;
    }
    /**
     *
     */
    private void initView(View view) {

//        mData = new LinkedList<>();
//        getCoursesList();
//        listAdapter = new DynamicItemListAdapter(getContext(),mData);
//        listView.setAdapter(listAdapter);
//        ListViewUtil.setListViewHeightBasedOnChildren(listView);

        imageButton_setting = (ImageButton) view.findViewById(R.id.imageButton_setting);

        r1 = (RelativeLayout) view.findViewById(R.id.a1);
        r2 = (RelativeLayout) view.findViewById(R.id.a2);
        r3 = (RelativeLayout) view.findViewById(R.id.a3);
        r4 = (RelativeLayout) view.findViewById(R.id.a4);
        r5 = (RelativeLayout) view.findViewById(R.id.a5);
        r6 = (RelativeLayout) view.findViewById(R.id.a6);
        r7 = (RelativeLayout) view.findViewById(R.id.a7);

    }

    /**
     * 为控件绑定事件
     */
    private void bindingEvent(){
        imageButton_setting.setOnClickListener(this);

        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
        r5.setOnClickListener(this);
        r6.setOnClickListener(this);
        r7.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButton_setting:
                System.out.println("点击了设置");
                Intent intent = new Intent(getActivity(), Setting.class);
                startActivity(intent);
                break;
            case R.id.a1:
            case R.id.a2:
            case R.id.a3:
            case R.id.a4:
            case R.id.a5:
            case R.id.a6:
            case R.id.a7:
                Intent intent1 = new Intent(getActivity(), Main2Activity.class);
                startActivity(intent1);
                break;
        }
    }


//    private void getCoursesList(){
//        DynamicItem dynamicItem = new DynamicItem();
//        dynamicItem.setUser_name("帅哥");
//        dynamicItem.setDynamic_create_time("2017");
//        dynamicItem.setDynamic_detail("2017年我要上精选！");
//        for(int i = 0; i < 6; i++){
//            mData.add(dynamicItem);
//        }
//    }

}
