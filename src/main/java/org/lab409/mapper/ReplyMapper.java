package org.lab409.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.lab409.entity.Article;
import org.lab409.entity.Reply;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Mapper
@Repository
public interface ReplyMapper {

    //添加评论到 forum_reply 表中
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

    //获取某篇文章的评论
    @Select("SELECT * FROM forum_reply WHERE TopicId=#{TopicId}")
    List<Reply> getReply(@Param("TopicId")Integer TopicId);
}
