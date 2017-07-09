package com.example.sun.keepfit.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by SUN on 2016/12/25.
 */
public class Course extends BmobObject{
    public Course(){
        course_id = "";
        title = "";
        time = "";
        detail = "";
    }
    public Course(String course_id,String title,String detail,String time){
        this.course_id = course_id;
        this.title = title;
        this.detail = detail;
        this.time = time;
    }
    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String course_id;
    private String title;
    private String detail;
    private String time;
}
