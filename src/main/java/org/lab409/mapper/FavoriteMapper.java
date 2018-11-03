package org.lab409.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.lab409.entity.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FavoriteMapper {

    @Insert("INSERT forum_favorite " +
            "(topic_id,user_id,collection_time) " +
            "VALUES " +
            "(#{topic_id},#{user_id},#{collection_time})")
    int collectTopic(Favorite favorite);
}
