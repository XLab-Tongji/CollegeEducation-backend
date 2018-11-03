package org.lab409.service.impl;

import org.lab409.entity.Article;
import org.lab409.entity.Favorite;
import org.lab409.entity.Reply;
import org.lab409.mapper.ArticleMapper;
import org.lab409.mapper.FavoriteMapper;
import org.lab409.mapper.ReplyMapper;
import org.lab409.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.binarywang.java.emoji.EmojiConverter;


import java.util.List;



/**
 * Created by jiao on 2018/10/16.
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    FavoriteMapper favoriteMapper;
    @Autowired
    ReplyMapper replyMapper;
    @Override
    public List<Article> getArticleBySectorAndKeyword(String SectorName,String SectorState,Integer userID,Integer SectorId, Integer page, Integer count,String keywords) {
        int start = (page - 1) * count;
        return articleMapper.getArticleBySectorAndKeyword(SectorName,SectorState,SectorId, start, count, userID,keywords);
    }

    @Override
    public List<Article> getArticle() {
        return articleMapper.getArticle();
    }

    @Override
    public boolean saveTopic(Article article){
        //emoji表情转换器，避免 Emoji 存储出现问题
        EmojiConverter emojiConverter=EmojiConverter.getInstance();
        //将article中的TopicText转换
        article.setTopicText(emojiConverter.toHtml(article.getTopicText()));
        if(articleMapper.saveTopic(article)!=1){
            return false;
        }
        return true;
    }

    //点赞文章
    @Override
    public boolean likeTopic(Article article){
        if(articleMapper.likeTopic(article)!=1){
            return false;
        }
        return true;
    }
    //收藏文章
    @Override
    public boolean collectTopic(Favorite favorite){
        if(favoriteMapper.collectTopic(favorite)!=1){
            return false;
        }
        return true;
    }
    //评论文章
    @Override
    public boolean replyTopic(Reply reply){
        if(replyMapper.replyTopic(reply)!=1){
            return false;
        }
        return true;
    }
}

