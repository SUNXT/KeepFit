package com.example.sun.keepfit.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.example.sun.keepfit.Bean.CommentItem;
import com.example.sun.keepfit.Bean.DynamicItem;
import com.example.sun.keepfit.R;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by SUN on 2016/12/12.
 */
public class DynamicItemListAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList<DynamicItem> mData;
    public DynamicItemListAdapter(Context context, LinkedList<DynamicItem> linkedList){
        mContext = context;
        mData = linkedList;
    }
    public void setmData(LinkedList<DynamicItem> data){
        mData = data;
    }
    public LinkedList<DynamicItem> getmData(){
        return mData;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public DynamicItem getItem(int i) {
        return mData.get(i);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateItem(int position,DynamicItem dynamicItem){
        mData.set(position,dynamicItem);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.dynamic_list_item, null);
            holder = new ViewHolder(convertView);
        }
        else
            holder = (ViewHolder) convertView.getTag();
         final DynamicItem item = getItem(position);
         holder.create_time.setText(item.getDynamic_create_time());
//        holder.user_icon.setImageBitmap(item.getUser_icon());
//        holder.dynamic_photo.setImageBitmap(item.getDynamic_photo());
        holder.tv_dynamic_detail.setText(item.getDynamic_detail());
        holder.tv_username.setText(item.getUser_name());
        return convertView;
    }

    class ViewHolder {
        TextView tv_username;
        TextView tv_dynamic_detail;
        ImageView dynamic_photo;
        ImageView user_icon;
        TextView create_time;


        public ViewHolder(View view) {
            tv_username = (TextView) view.findViewById(R.id.textView_dynamic_item_user_name);
            tv_dynamic_detail = (TextView) view.findViewById(R.id.textView_dynamic_item_detail);
            dynamic_photo = (ImageView) view.findViewById(R.id.imageView_dynamic_item_photo);
            user_icon = (ImageView) view.findViewById(R.id.imageView_dynamic_item_user_icon);
            create_time = (TextView) view.findViewById(R.id.textView_dynamic_item_create_time);
            view.setTag(this);
        }
    }
}
