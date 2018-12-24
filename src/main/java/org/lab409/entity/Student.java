package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/*
 ** created by jiao on 2018/12/24
 */
public class Student {
    private Integer student_id;
    private String student_no;
    private String student_name;
    private String student_sex;
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date student_birthday;
    private Integer student_school;
    private Integer student_major;
    private String student_email;

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public String getStudent_no() {
        return student_no;
    }

    public void setStudent_no(String student_no) {
        this.student_no = student_no;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_sex() {
        return student_sex;
    }

    public void setStudent_sex(String student_sex) {
        this.student_sex = student_sex;
    }

    public Date getStudent_birthday() {
        return student_birthday;
    }

    public void setStudent_birthday(Date student_birthday) {
        this.student_birthday = student_birthday;
    }

    public Integer getStudent_school() {
        return student_school;
    }

    public void setStudent_school(Integer student_school) {
        this.student_school = student_school;
    }

    public Integer getStudent_major() {
        return student_major;
    }

    public void setStudent_major(Integer student_major) {
        this.student_major = student_major;
    }

    public String getStudent_email() {
        return student_email;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }
}
