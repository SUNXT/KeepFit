package com.example.sun.keepfit.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.sun.keepfit.Adapter.CommentListAdapter;
import com.example.sun.keepfit.Bean.CommentItem;
import com.example.sun.keepfit.R;
import com.example.sun.keepfit.Util.ListViewUtil;

import java.util.LinkedList;

public class DynamicDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView listView;
    private LinkedList<CommentItem> mData;
    private CommentListAdapter commentListAdapter;

    private ImageButton imageButton_go_back;
    private Button button_attention;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dynamic_detail);
        initView();
    }
    private void initView(){
        listView = (ListView) findViewById(R.id.listView_show_comment);
        mData = new LinkedList<>();
        CommentItem commentItem;
        commentListAdapter = new CommentListAdapter(getApplicationContext(),mData);
        listView.setAdapter(commentListAdapter);
        for(int i = 0; i<2; i++){
            commentItem = new CommentItem();
            mData.add(commentItem);
        }
        ListViewUtil.setListViewHeightBasedOnChildren(listView);

        imageButton_go_back = (ImageButton) findViewById(R.id.imageButton_dynamic_detail_go_back);
        imageButton_go_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imageButton_dynamic_detail_go_back:
                finish();
                break;
        }
    }
}
