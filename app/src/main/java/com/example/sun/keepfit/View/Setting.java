package com.example.sun.keepfit.View;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.sun.keepfit.R;

import java.util.ArrayList;
import java.util.List;

public class Setting extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        listView = (ListView) findViewById(R.id.setting_list);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,setData()));
    }

    private List<String> setData(){

        List<String> data = new ArrayList<String>();
        data.add("个人资料");
        data.add("账号管理");
        data.add("隐私设置");
        data.add("关于我们");
        return data;
    }
}
