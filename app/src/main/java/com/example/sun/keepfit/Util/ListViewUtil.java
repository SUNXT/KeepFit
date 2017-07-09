package com.example.sun.keepfit.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenuListView;

/**
 * Created by SUN on 2016/12/31.
 */
public class ListViewUtil extends SwipeMenuListView {
    public ListViewUtil(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 对参数 listView 对象进行动态计算list view的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {


        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

    }

    /**
     *
     * @param gridView
     * @param numColumns 列数
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView ,int numColumns) {


        // 获取ListView对应的Adapter

        ListAdapter listAdapter = gridView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount() ; i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, gridView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

            System.out.println("每一项的高度："+listItem.getMeasuredHeight());
            if(i==listAdapter.getCount()-1&&listAdapter.getCount()%numColumns==1){
                System.out.println("满足条件："+numColumns);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();

        System.out.println("列数："+numColumns);

        params.height = totalHeight/numColumns;
//        (totalHeight
//                + (gridView.getMeasuredHeight() * (listAdapter.getCount())))/numColumns;
        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        System.out.println("高度："+params.height);
        gridView.setLayoutParams(params);

    }
}
