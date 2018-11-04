package org.lab409.entity;

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

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
