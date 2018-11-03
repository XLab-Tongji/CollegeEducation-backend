package org.lab409.entity;

import java.util.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/*
 ** created by jiao on 2018/10/25
 */

@Data
public class Blackboard {
    private Integer publish_id=0;    //暂时设定为0
    private Integer publish_type_id=1; //区分黑板报和文章，默认为1
    private Integer user_id;
    private Integer sector_id;       //分类id
    private String blackboard_name;
    private String blackboard_text;

    public String getBlackboard_text() {
        return blackboard_text;
    }

    public void setBlackboard_text(String blackboard_text) {
        this.blackboard_text = blackboard_text;
    }

    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date blackboard_date;
    private Integer reply_count;
    private Integer clicking_rate;
    private Integer praise_count;
    private Integer favorite_count;
}
