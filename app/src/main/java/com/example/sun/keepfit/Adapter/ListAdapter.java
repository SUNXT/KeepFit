package com.example.sun.keepfit.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.example.sun.keepfit.Bean.Course;
import com.example.sun.keepfit.R;

import java.util.LinkedList;

/**
 * Created by SUN on 2016/12/12.
 */
public class ListAdapter extends BaseSwipListAdapter {

    private Context mContext;
    private LinkedList<Course> mData;
    public ListAdapter(Context context, LinkedList<Course> linkedList){
        mContext = context;
        mData = linkedList;
    }
    public void setmData(LinkedList<Course> data){
        mData = data;
    }
    public LinkedList<Course> getmData(){
        return mData;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Course getItem(int i) {
        return mData.get(i);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateItem(int position,Course Course){
        mData.set(position,Course);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.course_list_item, null);
            holder = new ViewHolder(convertView);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        final Course item = getItem(position);
        holder.tv_time.setText(item.getTime());
        holder.tv_title.setText(item.getTitle());
        holder.tv_detail.setText(item.getDetail());
        return convertView;
    }

    class ViewHolder {
        TextView tv_title;
        TextView tv_time;
        TextView tv_detail;

        public ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.textView_course_title);
            tv_time = (TextView) view.findViewById(R.id.textView_course_time);
            tv_detail = (TextView) view.findViewById(R.id.textView_course_detail);
            view.setTag(this);
        }
    }

    @Override
    public boolean getSwipEnableByPosition(int position) {
        if(position % 2 == 0){
            return false;
        }
        return true;
    }
}
