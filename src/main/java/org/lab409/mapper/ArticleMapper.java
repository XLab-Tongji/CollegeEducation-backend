package org.lab409.mapper;

import org.apache.ibatis.annotations.*;

import org.lab409.entity.Article;
import org.lab409.entity.Favorite;
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
                + "a.sector_use_id,a.UserId,a.TopicId,c.`SectorName`,c.`SectorState`,a.`TopicTitle`,a.`TopicText`,a.`TopicDate`,a.`ReplyCount`,a.`ClickingRate`,a.PraiseCount,d.praise_id,e.favourite_id "
                + "FROM (forum_topic a LEFT JOIN (forum_sector_use b, forum_sector c) "
                + "ON b.sector_id=c.SectorId) "
                + "LEFT JOIN forum_praise d ON (a.TopicId=d.type_id AND d.user_id=#{userID}) "
                + "LEFT JOIN forum_favorite e ON (a.TopicId=e.topic_id AND e.user_id=#{userID}) "
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
            @Result(column="favourite_id",property="favourite_id")
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
    int saveTopic(Article article);

    //点赞某篇文章
    @Update("UPDATE forum_topic SET PraiseCount=PraiseCount+1 WHERE TopicId=#{TopicId}")
    int likeTopic(Article article);
    //取消点赞
    @Update("UPDATE forum_topic SET PraiseCount=PraiseCount-1 WHERE TopicId=#{TopicId}")
    int deletePraise(Article article);

    //collect a article
    @Update("UPDATE forum_topic SET favorite_count=favorite_count+1 WHERE TopicId=#{topic_id}")
    int collectTopic(Favorite favorite);
    //delete collection
    @Update("UPDATE forum_topic SET favorite_count=favorite_count-1 WHERE TopicId=#{topic_id}")
    int deleteCollection(Favorite favorite);
}
