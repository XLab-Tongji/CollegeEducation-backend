package org.lab409.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.lab409.entity.Blackboard;
import org.springframework.stereotype.Repository;

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
}
