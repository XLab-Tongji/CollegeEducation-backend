package org.lab409.mapper;

import org.apache.ibatis.annotations.*;
import org.lab409.entity.Blackboard;
import org.lab409.entity.Favorite;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 ** created by jiao on 2018/10/25
 */

@Mapper
@Repository
public interface BlackboardMapper {

    @Insert("INSERT INTO forum_blackboard " +
            "(publish_id," +
            "publish_type_id," +
            "user_id,sector_id," +
            "blackboard_name," +
            "blackboard_text," +
            "blackboard_date," +
            "reply_count," +
            "clicking_rate," +
            "praise_count," +
            "favorite_count) " +
            "VALUES (" +
            "#{publish_id}," +
            "#{publish_type_id}," +
            "#{user_id}," +
            "#{sector_id}," +
            "#{blackboard_name}," +
            "#{blackboard_text}," +
            "#{blackboard_date},"+
            "#{reply_count},"+
            "#{clicking_rate},"+
            "#{praise_count},"+
            "#{favorite_count})"
    )
    int saveBlackboard(Blackboard blackboard);


    @Select("<script>"
            + "SELECT "
            + "a.blackboard_id,a.user_id,a.sector_id,a.blackboard_name,a.blackboard_text,a.blackboard_date,a.reply_count,a.clicking_rate,a.praise_count,a.favorite_count,c.SectorName,d.praise_id,e.favourite_id,f.USERNAME "
            + "FROM (forum_blackboard a,forum_sector c) "
            + "LEFT JOIN USER f ON (a.user_id=f.ID) "
            + "LEFT JOIN forum_praise d ON (a.blackboard_id=d.type_id AND d.user_id=#{userID} AND d.type=1) "
            + "LEFT JOIN forum_favorite e ON (a.blackboard_id=e.topic_id AND e.user_id=#{userID} AND d.type=1) "
            + "WHERE a.sector_id=c.SectorId "
            + "<if test='SectorId==0'>"
            + "<if test='SectorName!=null'>"
            + "AND ("
            +   "<foreach collection='SectorName' item='item' index='index' separator=' OR '>"
            +       "c.SectorName LIKE concat('%',#{item},'%')"
            +   "</foreach>"
            + "OR "
            +   "<foreach collection='SectorName' item='item' index='index' separator=' OR '>"
            +       "a.blackboard_name LIKE concat('%',#{item},'%')"
            +   "</foreach>"
            + ") "
            + "</if>"
            + "</if>"
            + "<if test='SectorId==1'>"
            + "AND a.blackboard_name LIKE concat('%',#{keywords},'%')"
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
            + "ORDER BY a.blackboard_date DESC"
            + "</script>")
    List<Blackboard> getBlackboard(@Param("SectorName") String[] SectorName,@Param("SectorState") String SectorState,@Param("SectorId") Integer SectorId, @Param("userID") Integer userID,@Param("keywords") String keywords);


    @Update("UPDATE forum_blackboard SET praise_count=praise_count+1 WHERE blackboard_id=#{blackboard_id}")
    int praiseBlackboard(Blackboard blackboard);

    @Update("UPDATE forum_blackboard SET praise_count=praise_count-1 WHERE blackboard_id=#{blackboard_id}")
    int deletePraise(Blackboard blackboard);

    @Update("UPDATE forum_blackboard SET favorite_count=favorite_count+1 WHERE blackboard_id=#{topic_id}")
    int collectBlackboard(Favorite favorite);

    @Update("UPDATE forum_blackboard SET favorite_count=favorite_count-1 WHERE blackboard_id=#{topic_id}")
    int deleteCollection(Favorite favorite);
}
