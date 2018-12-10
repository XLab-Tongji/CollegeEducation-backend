package org.lab409.mapper;

import org.apache.ibatis.annotations.*;

import org.lab409.entity.Article;
import org.lab409.entity.ArticleOutput;
import org.lab409.entity.Favorite;
import org.lab409.entity.Reply;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jiao on 2018/10/16.
 */
@Mapper
@Repository
public interface ArticleMapper {

    @Select("<script>"
                + "SELECT "
                + "a.sector_use_id,a.UserId,a.TopicId,c.`SectorName`,c.`SectorState`,a.`TopicTitle`,a.`TopicText`,a.`TopicDate`,a.`ReplyCount`,a.`ClickingRate`,a.PraiseCount,d.praise_id,e.favourite_id,f.USERNAME "
                + "FROM (forum_topic a LEFT JOIN (forum_sector_use b, forum_sector c) "
                + "ON b.sector_id=c.SectorId) "
                + "LEFT JOIN USER f ON (a.UserId=f.ID) "
                + "LEFT JOIN forum_praise d ON (a.TopicId=d.type_id AND d.user_id=#{userID} AND d.type=0) "
                + "LEFT JOIN forum_favorite e ON (a.TopicId=e.topic_id AND e.user_id=#{userID} AND d.type=0) "
                + "WHERE a.TopicId=b.topic_id "
                + "<if test='SectorId==0'>"
                + "<if test='SectorName!=null'>"
                + "AND ("
                +   "<foreach collection='SectorName' item='item' index='index' separator=' OR '>"
                +       "c.SectorName LIKE concat('%',#{item},'%')"
                +   "</foreach>"
                + "OR "
                +   "<foreach collection='SectorName' item='item' index='index' separator=' OR '>"
                +       "a.TopicTitle LIKE concat('%',#{item},'%')"
                +   "</foreach>"
                + ") "
                + "</if>"
                + "</if>"
                + "<if test='SectorId==1'>"
                + "AND a.TopicTitle LIKE concat('%',#{keywords},'%')"
                + "</if>"
                + "<if test='SectorId==2'>"
                + "<if test='SectorName!=null'>"
                + "AND ("
                +   "<foreach collection='SectorName' item='item' index='index' separator=' OR '>"
                +       "c.SectorName LIKE concat('%',#{item},'%')"
                +   "</foreach>"
                + ") "
                + "</if>"
                + "</if>"
                + "ORDER BY a.TopicDate DESC"
                + "</script>")
    @Results({@Result(column="UserId",property="UserId"),
            @Result(column="TopicId",property="TopicId"),
            @Result(column="SectorName",property="SectorName"),
            @Result(column="SectorState",property="SectorState"),
            @Result(column="TopicTitle",property="TopicTitle"),
            @Result(column="TopicText",property="TopicText"),
            @Result(column="TopicDate",property="TopicDate"),
            @Result(column="ReplyCount",property="ReplyCount"),
            @Result(column="ClickingRate",property="ClickingRate"),
            @Result(column="PraiseCount",property="PraiseCount"),
            @Result(column="SectorId",property="SectorId"),
            @Result(column="praise_id",property="praise_id"),
            @Result(column="favourite_id",property="favourite_id"),
            @Result(column="USERNAME",property="USERNAME")
    })
    List<Article> getArticleBySectorAndKeyword(@Param("SectorName") String[] SectorName,@Param("SectorState") String SectorState,@Param("SectorId") Integer SectorId, @Param("userID") Integer userID,@Param("keywords") String keywords);

    @Select("SELECT * FROM forum_topic")
    @Results({@Result(column="TopicId",property="TopicId"),
            @Result(column="UserId",property="UserId"),
            @Result(column="TopicTitle",property="TopicTitle"),
            @Result(column="TopicText",property="TopicText"),
            @Result(column="TopicDate",property="TopicDate"),
            @Result(column="ReplyCount",property="ReplyCount"),
            @Result(column="ClickingRate",property="ClickingRate"),
            @Result(column="SectorId",property="SectorId"),
            @Result(column="PraiseCount",property="PraiseCount")
    })
    List<Article> getArticle();

    @Insert("INSERT INTO forum_topic " +
            "(publish_id," +
            "publish_type_id," +
            "UserId," +
            "sector_use_id," +
            "TopicTitle," +
            "TopicText," +
            "TopicDate," +
            "ReplyCount," +
            "ClickingRate," +
            "PraiseCount," +
            "favorite_count) " +
            "VALUES(" +
            "#{publish_id}," +
            "#{publish_type_id}," +
            "#{UserId}," +
            "#{sector_use_id}," +
            "#{TopicTitle}," +
            "#{TopicText}," +
            "#{TopicDate}," +
            "#{ReplyCount}," +
            "#{ClickingRate}," +
            "#{PraiseCount}," +
            "#{favorite_count})")
    @Options(useGeneratedKeys = true,keyProperty = "TopicId",keyColumn = "TopicId")
    int saveTopic(ArticleOutput articleoutput);

    //点赞某篇文章
    @Update("UPDATE forum_topic SET PraiseCount=PraiseCount+1 WHERE TopicId=#{TopicId}")
    int likeTopic(ArticleOutput articleOutput);
    //取消点赞
    @Update("UPDATE forum_topic SET PraiseCount=PraiseCount-1 WHERE TopicId=#{TopicId}")
    int deletePraise(ArticleOutput articleOutput);

    //collect an article
    @Update("UPDATE forum_topic SET favorite_count=favorite_count+1 WHERE TopicId=#{topic_id}")
    int collectTopic(Favorite favorite);
    //delete collection
    @Update("UPDATE forum_topic SET favorite_count=favorite_count-1 WHERE TopicId=#{topic_id}")
    int deleteCollection(Favorite favorite);

    //reply an article(increase ReplyCount)
    @Update("UPDATE forum_topic SET ReplyCount=ReplyCount+1 WHERE TopicId=#{TopicId}")
    int replyTopic(Reply reply);

    //browse an article(increase ClickingRate)
    @Update("UPDATE forum_topic SET ClickingRate=ClickingRate+1 WHERE TopicId=#{TopicId}")
    int browseTopic(ArticleOutput articleOutput);

    //get url of user image
    @Select("SELECT a.IMAGE FROM USER a WHERE a.ID=#{userID}")
    String getUrlOfUserImage(@Param(value = "userID")Integer userID);

    //update url of user image
    @Update("UPDATE USER a SET a.IMAGE=#{image} WHERE a.ID=#{userID}")
    int updateUrlOfUserImage(@Param(value = "image")String image,@Param(value = "userID")Integer userID);
}
