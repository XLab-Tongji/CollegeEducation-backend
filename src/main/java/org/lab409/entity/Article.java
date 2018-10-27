package org.lab409.entity;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * Created by jiao on 2018/10/16
 */
@Data
public class Article {
    private Integer publish_id=0;       //发布编号，暂时为0
    private Integer publish_type_id=0;  //发布类型编号，0位
    private Integer TopicId;            //在数据库中此为自增项

    @JsonProperty(value = "UserId")
    private Integer UserId;
    @JsonProperty(value = "TopicTitle")
    private String TopicTitle;
    @JsonProperty(value = "TopicText")
    private String TopicText;
    @JsonProperty(value = "TopicDate")
    private Timestamp TopicDate;
    @JsonProperty(value = "ReplyCount")
    private Integer ReplyCount;
    @JsonProperty(value = "ClickingRate")
    private Integer ClickingRate;
    @JsonProperty(value = "SectorId")
    private Integer SectorId;
    @JsonProperty(value = "PraiseCount")
    private Integer PraiseCount;
    @JsonProperty(value = "favorite_count")
    private Integer favorite_count;
    private String SectorName;
    private String SectorState;

    public String getTopicText() {
        return TopicText;
    }

    public void setTopicText(String topicText) {
        TopicText = topicText;
    }
}
