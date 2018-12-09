package org.lab409.entity;

import java.util.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/*
 ** created by jiao on 2018/10/25
 */

@Data
public class Blackboard {
    private Integer publish_id=0;    //暂时设定为0
    private Integer publish_type_id=1; //区分黑板报和文章，默认为1
    private Integer blackboard_id;
    private Integer user_id;
    private Integer sector_id;       //分类id
    private String blackboard_name;
    private String blackboard_text;
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date blackboard_date;
    private Integer reply_count;
    private Integer clicking_rate;
    private Integer praise_count;
    private Integer favorite_count;
    private Integer praise_id=-1;          //用来表示该黑板报是否被当前用户点赞，默认值为-1，代表没有被点赞
    private Integer favourite_id=-1;       //用来表示该黑板报是否被当前用户收藏，默认值为-1，代表没有被收藏
    private String SectorName;
    private String USERNAME;

    public String getBlackboard_text() {
        return blackboard_text;
    }
    public void setBlackboard_text(String blackboard_text) {
        this.blackboard_text = blackboard_text;
    }

    public Integer getPublish_id() {
        return publish_id;
    }

    public void setPublish_id(Integer publish_id) {
        this.publish_id = publish_id;
    }

    public Integer getPublish_type_id() {
        return publish_type_id;
    }

    public void setPublish_type_id(Integer publish_type_id) {
        this.publish_type_id = publish_type_id;
    }

    public Integer getBlackboard_id() {
        return blackboard_id;
    }

    public void setBlackboard_id(Integer blackboard_id) {
        this.blackboard_id = blackboard_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getSector_id() {
        return sector_id;
    }

    public void setSector_id(Integer sector_id) {
        this.sector_id = sector_id;
    }

    public String getBlackboard_name() {
        return blackboard_name;
    }

    public void setBlackboard_name(String blackboard_name) {
        this.blackboard_name = blackboard_name;
    }

    public Date getBlackboard_date() {
        return blackboard_date;
    }

    public void setBlackboard_date(Date blackboard_date) {
        this.blackboard_date = blackboard_date;
    }

    public Integer getReply_count() {
        return reply_count;
    }

    public void setReply_count(Integer reply_count) {
        this.reply_count = reply_count;
    }

    public Integer getClicking_rate() {
        return clicking_rate;
    }

    public void setClicking_rate(Integer clicking_rate) {
        this.clicking_rate = clicking_rate;
    }

    public Integer getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(Integer praise_count) {
        this.praise_count = praise_count;
    }

    public Integer getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(Integer favorite_count) {
        this.favorite_count = favorite_count;
    }

    public Integer getPraise_id() {
        return praise_id;
    }

    public void setPraise_id(Integer praise_id) {
        this.praise_id = praise_id;
    }

    public Integer getFavourite_id() {
        return favourite_id;
    }

    public void setFavourite_id(Integer favourite_id) {
        this.favourite_id = favourite_id;
    }

    public String getSectorName() {
        return SectorName;
    }

    public void setSectorName(String sectorName) {
        SectorName = sectorName;
    }

    @JsonProperty(value = "USERNAME")
    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }
}
