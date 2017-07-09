package com.example.sun.keepfit.Bean;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobObject;

/**
 * Created by SUN on 2017/1/2.
 */
public class DynamicItem extends BmobObject {

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public Bitmap getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(Bitmap user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDynamic_detail() {
        return dynamic_detail;
    }

    public void setDynamic_detail(String dynamic_detail) {
        this.dynamic_detail = dynamic_detail;
    }

    public Bitmap getDynamic_photo() {
        return dynamic_photo;
    }

    public void setDynamic_photo(Bitmap dynamic_photo) {
        this.dynamic_photo = dynamic_photo;
    }

    public String getDynamic_create_time() {
        return dynamic_create_time;
    }

    public void setDynamic_create_time(String dynamic_create_time) {
        this.dynamic_create_time = dynamic_create_time;
    }

    public int getThumb_up_num() {
        return thumb_up_num;
    }

    public void setThumb_up_num(int thumb_up_num) {
        this.thumb_up_num = thumb_up_num;
    }

    private String user_account;
    private String user_name;
    private Bitmap user_icon;
    private String dynamic_detail;
    private Bitmap dynamic_photo;
    private String dynamic_create_time;
    private int thumb_up_num;
    public DynamicItem(){
        this.thumb_up_num = 0;
        this.dynamic_photo = null;
    }

}
