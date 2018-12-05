package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.util.Date;

/**
 * Created by jiao on 2018/12/4
 */

public class Question {
    private Integer question_ID;
    private Integer question_sector_ID;
    private String question_title;
    private String sector_name;
    private Integer user_ID;
    private String question_text;
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date question_date;
    private Integer answer_ID;
    private Integer answer_count;
    private Integer clicking_rate;
    private Float question_evaluation;
    private Integer question_follow;
    private Integer question_participate;
    private String picture_path;


    public Integer getQuestion_ID() {
        return question_ID;
    }

    public void setQuestion_ID(Integer question_ID) {
        this.question_ID = question_ID;
    }

    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    public Integer getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(Integer user_ID) {
        this.user_ID = user_ID;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public Date getQuestion_date() {
        return question_date;
    }

    public void setQuestion_date(Date question_date) {
        this.question_date = question_date;
    }

    public Integer getAnswer_ID() {
        return answer_ID;
    }

    public void setAnswer_ID(Integer answer_ID) {
        this.answer_ID = answer_ID;
    }

    public Integer getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(Integer answer_count) {
        this.answer_count = answer_count;
    }

    public Integer getClicking_rate() {
        return clicking_rate;
    }

    public void setClicking_rate(Integer clicking_rate) {
        this.clicking_rate = clicking_rate;
    }

    public Float getQuestion_evaluation() {
        return question_evaluation;
    }

    public void setQuestion_evaluation(Float question_evaluation) {
        this.question_evaluation = question_evaluation;
    }

    public Integer getQuestion_follow() {
        return question_follow;
    }

    public void setQuestion_follow(Integer question_follow) {
        this.question_follow = question_follow;
    }

    public String getSector_name() {
        return sector_name;
    }

    public void setSector_name(String sector_name) {
        this.sector_name = sector_name;
    }

    public Integer getQuestion_participate() {
        return question_participate;
    }

    public void setQuestion_participate(Integer question_participate) {
        this.question_participate = question_participate;
    }

    public String getPicture_path() {
        return picture_path;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public Integer getQuestion_sector_ID() {
        return question_sector_ID;
    }

    public void setQuestion_sector_ID(Integer question_sector_ID) {
        this.question_sector_ID = question_sector_ID;
    }
}
