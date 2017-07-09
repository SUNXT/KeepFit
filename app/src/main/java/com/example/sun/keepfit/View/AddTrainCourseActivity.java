package com.example.sun.keepfit.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sun.keepfit.Adapter.ListAdapter;
import com.example.sun.keepfit.Bean.Course;
import com.example.sun.keepfit.R;

import java.util.LinkedList;

public class AddTrainCourseActivity extends AppCompatActivity implements View.OnClickListener{

    private ListAdapter listAdapter;
    private ListView listView;
    private LinkedList<Course> mData;
    private ImageButton imageButton_go_back;
    private TextView textView_search_course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_train_course);
        initView();
        bindingEvent();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        /**
         * 初始化列表控件
         */
        listView = (ListView) findViewById(R.id.listView_show_courses);
        mData = new LinkedList<>();
        getCoursesList();
        listAdapter = new ListAdapter(getApplicationContext(),mData);

        listView.setAdapter(listAdapter);

        /**
         * 返回按钮 和 搜索
         */
        imageButton_go_back = (ImageButton) findViewById(R.id.imageButton_add_train_go_back);
        textView_search_course = (TextView) findViewById(R.id.textView_add_course_serach);
    }

    /**
     * 为控件绑定控件
     */
    private void bindingEvent(){
        imageButton_go_back.setOnClickListener(this);
        textView_search_course.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course item = mData.get(i);
                Intent intent = new Intent(getApplicationContext(),CourseDetailActivity.class);
                intent.putExtra("course",item);
                startActivity(intent);
            }
        });
    }

    /**
     * 通过网络获取教学课程列表
     */
    private void getCoursesList(){
        Course course = new Course("","标题","123","1232");
        for(int i = 0; i < 20; i++){
            mData.add(course);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButton_add_train_go_back:
                finish();
                break;
            case R.id.textView_add_course_serach:
                break;
        }
    }
}
