package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jiao on 2018/11/14
 */
@Data
public class ArticleOutput {
    private Integer publish_id=0;       //发布编号，暂时为0
    private Integer publish_type_id=0;  //发布类型编号，0位

    private Integer TopicId;            //在数据库中此为自增项
    private Integer UserId;
    private String TopicTitle;
    private String TopicText;


    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date TopicDate;


    private Integer ReplyCount;

    private Integer ClickingRate;

    private Integer PraiseCount;

    private Integer favorite_count;

    private Integer sector_use_id=0;       //暂设默认值为0，该字段暂时没什么用
    private List<String> SectorName=new ArrayList<>();
    private String SectorState;
    private Integer praise_id=-1;          //用来表示该文章是否被当前用户点赞，默认值为-1，代表没有被点赞
    private Integer favourite_id=-1;       //用来表示该文章是否被当前用户收藏，默认值为-1，代表没有被收藏

    @JsonProperty(value = "TopicText")
    public String getTopicText() {
        return TopicText;
    }

    public void setTopicText(String topicText) {
        TopicText = topicText;
    }
    @JsonProperty(value="TopicId")
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
    @JsonProperty(value = "TopicTitle")
    public String getTopicTitle() {
        return TopicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        TopicTitle = topicTitle;
    }
    @JsonProperty(value = "TopicDate")
    public Date getTopicDate() {
        return TopicDate;
    }

    public void setTopicDate(Date topicDate) {
        TopicDate = topicDate;
    }
    @JsonProperty(value = "ReplyCount")
    public Integer getReplyCount() {
        return ReplyCount;
    }

    public void setReplyCount(Integer replyCount) {
        ReplyCount = replyCount;
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
    @JsonProperty(value = "favorite_count")
    public Integer getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(Integer favorite_count) {
        this.favorite_count = favorite_count;
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

    public Integer getSector_use_id() {
        return sector_use_id;
    }

    public void setSector_use_id(Integer sector_use_id) {
        this.sector_use_id = sector_use_id;
    }

    public List<String> getSectorName() {
        return SectorName;
    }

    public void setSectorName(List<String> sectorName) {
        SectorName = sectorName;
    }

    public String getSectorState() {
        return SectorState;
    }

    public void setSectorState(String sectorState) {
        SectorState = sectorState;
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
}
