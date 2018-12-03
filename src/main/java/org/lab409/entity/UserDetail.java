package org.lab409.entity;

import lombok.Data;

@Data
public class UserDetail {
    Integer userID;
    String username;

    //我的上传个数
    Integer myUploadNum;

    //我的下载个数
    Integer myDownloadNum;

    //我收藏的个数
    Integer myFavouriteNum;

    //我推荐的个数
    Integer mySuggestedNum;

    Integer leftPoints;

    //我所有上传的资源获得的平均分
    Double avgScore;

    //我上传资源的获赞率
    Double suggestedRate;

    public UserDetail() {

    }


    public UserDetail(Integer userID, String username, Integer myUploadNum, Integer myDownloadNum, Integer myFavouriteNum,
                      Integer mySuggestedNum, Double avgScore, Double suggestedRate, Integer leftPoints) {
        this.userID = userID;
        this.username = username;
        this.myUploadNum = myUploadNum;
        this.myDownloadNum = myDownloadNum;
        this.myFavouriteNum = myFavouriteNum;
        this.mySuggestedNum = mySuggestedNum;
        this.avgScore = avgScore;
        this.suggestedRate = suggestedRate;
        this.leftPoints = leftPoints;
    }
}
