package org.lab409.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.lab409.entity.Praise;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PraiseMapper {

    //点赞
    @Insert("INSERT forum_praise " +
            "(type_id," +
            "type," +
            "user_id," +
            "praise_date) " +
            "VALUES " +
            "(#{type_id}," +
            "#{type}," +
            "#{user_id}," +
            "#{praise_date})")
    int addPraise(Praise praise);

    //取消点赞
    @Delete("DELETE FROM forum_praise WHERE type_id=#{type_id} AND user_id=#{user_id}")
    int deletePraise(Praise praise);
}
