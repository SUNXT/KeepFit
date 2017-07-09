package com.example.sun.keepfit.Adapter;

import android.widget.BaseAdapter;

/**
 * Created by SUN on 2016/12/12.
 */
public abstract class BaseSwipListAdapter extends BaseAdapter {
    public boolean getSwipEnableByPosition(int position){
        return true;
    }
}
