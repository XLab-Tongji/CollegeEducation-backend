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
    private Integer topic_id;       //关联的文章或黑板报id
    @JsonProperty(value = "user_id")
    private Integer user_id;        //收藏用户id
    @JsonProperty(value = "collection_time")
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date collection_time;   //收藏的时间
    @JsonProperty(value = "type")
    private Integer type;           //区分是文章还是黑板报，0 为文章（默认），1 为黑板报
}
