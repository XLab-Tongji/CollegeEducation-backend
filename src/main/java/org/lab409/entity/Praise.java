package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
//点赞表
public class Praise {
    private Integer praise_id;            //自增主键
    private Integer type_id;              //对应文章(topicId)或评论(replyId)的编号
    private Integer type;                 //点赞的类型，0表示为文章点赞，1表示为评论点赞
    private Integer user_id;              //点赞人的id
    private Date praise_date=new Date();  //点赞的时间

    @JsonProperty(value = "type_id")
    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    @JsonProperty(value = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonProperty(value = "user_id")
    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @JsonProperty(value = "praise_id")
    public Integer getPraise_id() {
        return praise_id;
    }

    public void setPraise_id(Integer praise_id) {
        this.praise_id = praise_id;
    }

    @JsonProperty(value = "praise_date")
    public Date getPraise_date() {
        return praise_date;
    }

    public void setPraise_date(Date praise_date) {
        this.praise_date = praise_date;
    }
}
