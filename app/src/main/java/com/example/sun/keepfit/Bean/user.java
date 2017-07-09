package com.example.sun.keepfit.Bean;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by rewrite123 on 2016/12/17.
 */
public class user extends BmobObject{
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BmobFile getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(BmobFile user_icon) {
        this.user_icon = user_icon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String account;
    private String username;
    private String password;
    private BmobFile user_icon;

}