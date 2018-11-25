package org.lab409.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.lab409.entity.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FavoriteMapper {

    @Insert("INSERT forum_favorite " +
            "(topic_id,user_id,collection_time,type) " +
            "VALUES " +
            "(#{topic_id},#{user_id},#{collection_time},#{type})")
    int collectTopic(Favorite favorite);

    //delete collection
    @Delete("DELETE FROM forum_favorite WHERE topic_id=#{topic_id} AND user_id=#{user_id} AND type=#{type}")
    int deleteCollection(Favorite favorite);
}
