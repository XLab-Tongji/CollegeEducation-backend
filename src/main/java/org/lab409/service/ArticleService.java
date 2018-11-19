package org.lab409.service;

import org.lab409.entity.Article;
import org.lab409.entity.ArticleOutput;
import org.lab409.entity.Favorite;
import org.lab409.entity.Reply;

import java.util.List;

public interface ArticleService {
    List<ArticleOutput> getArticleBySectorAndKeyword(String[] SectorName, String SectorState, Integer userID, Integer SectorId, String keywords);
    List<Article> getArticle();
    List<Reply> getReply(Integer TopicId);  //获取某篇文章的评论
    boolean saveTopic(Article article);     //存文章
    boolean likeTopic(Article article,Integer userID);     //点赞
    boolean deletePraise(Article article,Integer userID);  //取消点赞
    boolean collectTopic(Favorite favorite);//收藏
    boolean deleteCollection(Favorite favorite);//delete collection
    boolean replyTopic(Reply reply);        //评论
}
