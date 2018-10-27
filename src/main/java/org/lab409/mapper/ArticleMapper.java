package org.lab409.mapper;

import org.apache.ibatis.annotations.*;

import org.lab409.entity.Article;
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
                + "a.SectorId,a.UserId,a.TopicId,b.`SectorName`,b.`SectorState`,a.`TopicTitle`,a.`TopicText`,a.`TopicDate`,a.`ReplyCount`,a.`ClickingRate`,a.PraiseCount "
                + "FROM forum_topic a, forum_sector b "
                + "WHERE a.SectorId=b.SectorID "
                + "<if test='userID!=0'>"
                + "AND a.UserId=#{userID}"
                + "</if>"
                + "<if test='keywords!=null'>"
                + "AND a.TopicTitle LIKE concat('%',#{keywords},'%')"
                + "</if>"
                + "<if test='SectorName!=null'>"
                + "AND b.SectorName LIKE concat('%',#{SectorName},'%')"
                + "</if>"
                + "<if test='SectorState!=null'>"
                + "AND b.SectorState LIKE concat('%',#{SectorState},'%')"
                + "</if>"
                + "ORDER BY a.TopicDate DESC limit #{start},#{count}"
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
            @Result(column="SectorId",property="SectorId")
    })
    List<Article> getArticleBySectorAndKeyword(@Param("SectorName") String SectorName,@Param("SectorState") String SectorState,@Param("SectorId") Integer SectorId, @Param("start") Integer start, @Param("count") Integer count, @Param("userID") Integer userID,@Param("keywords") String keywords);

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
            "SectorId," +
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
            "#{SectorId}," +
            "#{TopicTitle}," +
            "#{TopicText}," +
            "#{TopicDate}," +
            "#{ReplyCount}," +
            "#{ClickingRate}," +
            "#{PraiseCount}," +
            "#{favorite_count})")
    int saveTopic(Article article);
}
