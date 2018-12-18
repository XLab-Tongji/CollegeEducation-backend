package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Data
@Repository
public class Reply {

    private String USERNAME;        //回复用户的用户名

    private Integer ReplyId;        //自增主键

    private Integer TopicId;        //关联的文章Id

    private Integer UserId;         //回复用户Id

    private String ReplyText;       //回复内容

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ReplyDate;         //回复时间

    private Integer ClickingRate;   //点击数

    private Integer PraiseCount;    //点赞数

    private Integer type;           //区分文章和黑板报，0为文章（默认），1为黑板报

    @JsonProperty(value = "ReplyId")
    public Integer getReplyId() {
        return ReplyId;
    }

    public void setReplyId(Integer replyId) {
        ReplyId = replyId;
    }

    @JsonProperty(value = "TopicId")
    public Integer getTopicId() {
        return TopicId;
    }

    public void setTopicId(Integer topicId) {
        TopicId = topicId;
    }

    @JsonProperty(value = "UserId")
    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    @JsonProperty(value = "ReplyText")
    public String getReplyText() {
        return ReplyText;
    }

    public void setReplyText(String replyText) {
        ReplyText = replyText;
    }

    @JsonProperty(value = "ReplyDate")
    public Date getReplyDate() {
        return ReplyDate;
    }

    public void setReplyDate(Date replyDate) {
        ReplyDate = replyDate;
    }

    @JsonProperty(value = "ClickingRate")
    public Integer getClickingRate() {
        return ClickingRate;
    }

    public void setClickingRate(Integer clickingRate) {
        ClickingRate = clickingRate;
    }

    @JsonProperty(value = "PraiseCount")
    public Integer getPraiseCount() {
        return PraiseCount;
    }

    public void setPraiseCount(Integer praiseCount) {
        PraiseCount = praiseCount;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
