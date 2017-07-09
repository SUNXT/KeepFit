package com.example.sun.keepfit.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by SUN on 2016/12/31.
 */
public class TrainCourse extends BmobObject{
    private BmobFile photo;//课程介绍缩略图
    private BmobFile video;//课程教学视频
    private String title;//标题
    private String course_num;//节数
    private String total_time;//课程时间
    private String total_fat;//课程消耗的能量
    private String introduce;//课程介绍

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public BmobFile getVideo() {
        return video;
    }

    public void setVideo(BmobFile video) {
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal_fat() {
        return total_fat;
    }

    public void setTotal_fat(String total_fat) {
        this.total_fat = total_fat;
    }

    public String getCourse_num() {
        return course_num;
    }

    public void setCourse_num(String course_num) {
        this.course_num = course_num;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

}
