package com.example.sun.keepfit.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sun.keepfit.Bean.ChoicenessItem;
import com.example.sun.keepfit.Bean.Course;
import com.example.sun.keepfit.R;

import java.util.LinkedList;

/**
 * Created by SUN on 2017/1/1.
 */
public class ChoicenessListAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList<ChoicenessItem> mData;
    public ChoicenessListAdapter(Context context, LinkedList<ChoicenessItem> linkedList){
        mContext = context;
        mData = linkedList;
    }
    public void setmData(LinkedList<ChoicenessItem> data){
        mData = data;
    }
    public LinkedList<ChoicenessItem> getmData(){
        return mData;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ChoicenessItem getItem(int i) {
        return mData.get(i);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateItem(int position,ChoicenessItem choicenessItem){
        mData.set(position,choicenessItem);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.choiceness_list_item, null);
            holder = new ViewHolder(convertView);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        final ChoicenessItem item = getItem(position);
        holder.tv_message_num.setText(item.getMessage_num());
        holder.tv_thumb_up_num.setText(item.getThumb_up_num());
        holder.tv_detail.setText(item.getDetail());
//        holder.imageView_photo.setImageBitmap(item.getPhoto());
        return convertView;
    }

    class ViewHolder {
        TextView tv_thumb_up_num;
        TextView tv_message_num;
        TextView tv_detail;
        ImageView imageView_photo;
        public ViewHolder(View view) {
            tv_thumb_up_num = (TextView) view.findViewById(R.id.tv_choiceness_item_thumb_up_num);
            tv_message_num = (TextView) view.findViewById(R.id.tv_choiceness_item_message_num);
            tv_detail = (TextView) view.findViewById(R.id.tv_choiceness_item_detail);
            imageView_photo = (ImageView) view.findViewById(R.id.imageView_choiceness_item_photo);
            view.setTag(this);
        }
    }
}
