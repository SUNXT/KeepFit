package com.example.sun.keepfit.Bean;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobObject;

/**
 * Created by SUN on 2017/1/1.
 */
public class CommentItem extends BmobObject{
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Bitmap getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(Bitmap user_photo) {
        this.user_photo = user_photo;
    }

    private String username;
    private String comment;
    private Bitmap user_photo;
    public CommentItem(){
        this.comment = "";
        this.username = "用户";
        this.user_photo = null;
    }
    public CommentItem(String username,String comment,Bitmap bitmap){
        this.user_photo = bitmap;
        this.username = username;
        this.comment = comment;
    }
}
