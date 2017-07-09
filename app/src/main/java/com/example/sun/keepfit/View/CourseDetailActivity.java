package com.example.sun.keepfit.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.sun.keepfit.R;

public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton imageButton_back;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_course_detail);
        initView();
        bindingEvents();
    }

    private void initView(){
        imageButton_back = (ImageButton) findViewById(R.id.imageButton_dynamic_detail_go_back);
        button = (Button) findViewById(R.id.button_add_course_to_train);
    }
    private void bindingEvents(){
        imageButton_back.setOnClickListener(this);
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButton_dynamic_detail_go_back:
                finish();
                break;
            case R.id.button_add_course_to_train:

                finish();
                break;
        }
    }
}
