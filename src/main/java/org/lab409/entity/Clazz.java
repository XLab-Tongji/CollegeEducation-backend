package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
/*
 ** created by jiao on 2018/12/24
 */
public class Clazz {
    private Integer course_id;
    private String course_no;
    private String course_name;
    private String course_term;
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date course_start_date;
    private String course_signup_link;
    private String course_access_code;
    private String course_class_link;
    private Integer course_instructor_self_signup;
    private Integer course_class_status;
    private String course_credit;

    private Integer admin_id;
    private String admin_name;
    private String admin_class;

    private Integer admin_userid;

    public Integer getAdmin_userid() {
        return admin_userid;
    }

    public void setAdmin_userid(Integer admin_userid) {
        this.admin_userid = admin_userid;
    }

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    public String getCourse_no() {
        return course_no;
    }

    public void setCourse_no(String course_no) {
        this.course_no = course_no;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_term() {
        return course_term;
    }

    public void setCourse_term(String course_term) {
        this.course_term = course_term;
    }

    public Date getCourse_start_date() {
        return course_start_date;
    }

    public void setCourse_start_date(Date course_start_date) {
        this.course_start_date = course_start_date;
    }

    public String getCourse_signup_link() {
        return course_signup_link;
    }

    public void setCourse_signup_link(String course_signup_link) {
        this.course_signup_link = course_signup_link;
    }

    public String getCourse_access_code() {
        return course_access_code;
    }

    public void setCourse_access_code(String course_access_code) {
        this.course_access_code = course_access_code;
    }

    public String getCourse_class_link() {
        return course_class_link;
    }

    public void setCourse_class_link(String course_class_link) {
        this.course_class_link = course_class_link;
    }

    public Integer getCourse_instructor_self_signup() {
        return course_instructor_self_signup;
    }

    public void setCourse_instructor_self_signup(Integer course_instructor_self_signup) {
        this.course_instructor_self_signup = course_instructor_self_signup;
    }

    public Integer getCourse_class_status() {
        return course_class_status;
    }

    public void setCourse_class_status(Integer course_class_status) {
        this.course_class_status = course_class_status;
    }

    public String getCourse_credit() {
        return course_credit;
    }

    public void setCourse_credit(String course_credit) {
        this.course_credit = course_credit;
    }

    public Integer getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(Integer admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_class() {
        return admin_class;
    }

    public void setAdmin_class(String admin_class) {
        this.admin_class = admin_class;
    }
}
