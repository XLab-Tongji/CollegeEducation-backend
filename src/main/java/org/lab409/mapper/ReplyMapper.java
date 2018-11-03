package org.lab409.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.lab409.entity.Reply;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Mapper
@Repository
public interface ReplyMapper {

    //添加回复到 forum_reply 表中
    @Insert("INSERT forum_reply " +
            "(TopicId," +
            "UserId," +
            "ReplyText," +
            "ReplyDate," +
            "ClickingRate," +
            "PraiseCount) " +
            "VALUES " +
            "(#{TopicId}," +
            "#{UserId}," +
            "#{ReplyText}," +
            "#{ReplyDate}," +
            "#{ClickingRate}," +
            "#{PraiseCount})")
    int replyTopic(Reply reply);
}
