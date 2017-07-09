package com.example.sun.keepfit.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sun.keepfit.Bean.CommentItem;
import com.example.sun.keepfit.Bean.Course;
import com.example.sun.keepfit.R;

import java.util.LinkedList;

/**
 * Created by SUN on 2016/12/12.
 */
public class CommentListAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList<CommentItem> mData;
    public CommentListAdapter(Context context, LinkedList<CommentItem> linkedList){
        mContext = context;
        mData = linkedList;
    }
    public void setmData(LinkedList<CommentItem> data){
        mData = data;
    }
    public LinkedList<CommentItem> getmData(){
        return mData;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CommentItem getItem(int i) {
        return mData.get(i);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateItem(int position,CommentItem commentItem){
        mData.set(position,commentItem);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.comment_list_item, null);
            holder = new ViewHolder(convertView);
        }
        else
            holder = (ViewHolder) convertView.getTag();
//        final Course item = getItem(position);
//        holder.tv_time.setText(item.getTime());
//        holder.tv_title.setText(item.getTitle());
//        holder.tv_detail.setText(item.getDetail());
        return convertView;
    }

    class ViewHolder {
        TextView tv_username;
        TextView tv_comment;
        ImageView img_photo;

        public ViewHolder(View view) {
            tv_username = (TextView) view.findViewById(R.id.textView_comment_user_name);
            tv_comment = (TextView) view.findViewById(R.id.textView_comment_detail);
            img_photo = (ImageView) view.findViewById(R.id.imageView_comment_user_photo);
            view.setTag(this);
        }
    }
}
