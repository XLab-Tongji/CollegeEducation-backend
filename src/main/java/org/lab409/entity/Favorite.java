package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.sql.Timestamp;

@Data
public class Favorite {
    @JsonProperty(value = "favourite_id")
    private Integer favourite_id;   //自增主键
    @JsonProperty(value = "topic_id")
    private Integer topic_id;       //关联的文章id
    @JsonProperty(value = "user_id")
    private Integer user_id;        //收藏用户id
    @JsonProperty(value = "collection_time")
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date collection_time;   //收藏的时间
}
