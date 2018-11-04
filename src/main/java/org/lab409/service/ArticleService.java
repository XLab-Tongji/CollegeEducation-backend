package org.lab409.service;

import org.lab409.entity.Article;
import org.lab409.entity.Favorite;
import org.lab409.entity.Reply;

import java.util.List;

public interface ArticleService {
    List<Article> getArticleBySectorAndKeyword(String SectorName,String SectorState,Integer userID,Integer SectorId, Integer page, Integer count,String keywords);
    List<Article> getArticle();
    boolean saveTopic(Article article);     //存文章
    boolean likeTopic(Article article,Integer userID);     //点赞
    boolean collectTopic(Favorite favorite);//收藏
    boolean replyTopic(Reply reply);        //回复
}
