package com.example.sun.keepfit.Bean;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobObject;

/**
 * Created by SUN on 2017/1/1.
 */
public class ChoicenessItem extends BmobObject{
    private String detail;
    private String thumb_up_num;
    private String message_num;
    private Bitmap photo;

    public ChoicenessItem(){}
    public ChoicenessItem(String detail,String thumb_up_num,String message_num,Bitmap bitmap){
        this.detail = detail;
        this.thumb_up_num = thumb_up_num;
        this.message_num = message_num;
        photo = bitmap;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getThumb_up_num() {
        return thumb_up_num;
    }

    public void setThumb_up_num(String thumb_up_num) {
        this.thumb_up_num = thumb_up_num;
    }

    public String getMessage_num() {
        return message_num;
    }

    public void setMessage_num(String message_num) {
        this.message_num = message_num;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
