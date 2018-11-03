package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Data
@Repository
public class Reply {
    @JsonProperty(value = "ReplyId")
    private Integer ReplyId;        //自增主键
    @JsonProperty(value = "TopicId")
    private Integer TopicId;        //关联的文章Id
    @JsonProperty(value = "UserId")
    private Integer UserId;         //回复用户Id
    @JsonProperty(value = "ReplyText")
    private String ReplyText;       //回复内容
    @JsonProperty(value = "ReplyDate")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ReplyDate;         //回复时间
    @JsonProperty(value = "ClickingRate")
    private Integer ClickingRate;   //点击数
    @JsonProperty(value = "PraiseCount")
    private Integer PraiseCount;    //点赞数
}
