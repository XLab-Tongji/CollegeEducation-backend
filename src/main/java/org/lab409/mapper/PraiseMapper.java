package org.lab409.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.lab409.entity.Praise;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PraiseMapper {
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
}
