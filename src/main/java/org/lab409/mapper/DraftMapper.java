package org.lab409.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.lab409.entity.Draft;

/*
 ** created by jiao on 2018/10/26
 */

@Mapper
@Repository
public interface DraftMapper {
    @Insert("INSERT INTO forum_draft " +
            "(state_id," +
            "publish_type_id," +
            "user_id," +
            "sector_id," +
            "draft_name," +
            "draft_text," +
            "write_date) " +
            "VALUES(" +
            "#{state_id}," +
            "#{publish_type_id}," +
            "#{user_id}," +
            "#{sector_id}," +
            "#{draft_name}," +
            "#{draft_text}," +
            "#{write_date})")
    int saveDraft(Draft draft);
}
