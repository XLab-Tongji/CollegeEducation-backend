package org.lab409.entity;

import lombok.Data;

@Data
public class ResourceDetail {

    UploaderInfo uploaderInfo;

    //下载次数
    Integer downloadTimes;

    //收藏次数
    Integer favouriteNum;

    SuggestInfo suggestInfo;

    CommentInfo commentInfo;

    //上传人平均评分
    Double uploaderAvgScore;

    //上传人的获赞率
    Double uploaderSuggestedRate;

    public ResourceDetail() {

    }

    public ResourceDetail(UploaderInfo uploaderInfo, SuggestInfo suggestInfo, CommentInfo commentInfo,
                          Integer downloadTimes, Integer favouriteNum,
                          Double uploaderAvgScore, Double uploaderSuggestedRate) {
        this.uploaderInfo = uploaderInfo;
        this.suggestInfo = suggestInfo;
        this.commentInfo = commentInfo;
        this.downloadTimes = downloadTimes;
        this.favouriteNum = favouriteNum;
        this.uploaderAvgScore = uploaderAvgScore;
        this.uploaderSuggestedRate = uploaderSuggestedRate;
    }

    @Data
    public static class UploaderInfo {
        String resourceID;
        String uploaderName;
        Integer uploaderID;
    }

    @Data
    public static class CommentInfo {
        //资源评论数
        Integer commentNum;

        //资源平均分
        Double resourceAvgScore;
    }

    @Data
    public static class SuggestInfo {
        //资源推荐数
        Integer suggestedNum;

        //资源好评率
        Double resourceSuggestedRate;
    }
}
