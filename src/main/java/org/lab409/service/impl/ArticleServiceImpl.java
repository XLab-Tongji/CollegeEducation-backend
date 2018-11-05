package org.lab409.service.impl;

import org.lab409.entity.Article;
import org.lab409.entity.Favorite;
import org.lab409.entity.Praise;
import org.lab409.entity.Reply;
import org.lab409.mapper.ArticleMapper;
import org.lab409.mapper.FavoriteMapper;
import org.lab409.mapper.PraiseMapper;
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
    @Autowired
    PraiseMapper praiseMapper;
    @Override
    public List<Article> getArticleBySectorAndKeyword(String[] SectorName,String SectorState,Integer userID,Integer SectorId,String keywords) {
        return articleMapper.getArticleBySectorAndKeyword(SectorName,SectorState,SectorId, userID,keywords);
    }

    @Override
    public List<Article> getArticle() {
        return articleMapper.getArticle();
    }

    //保存文章
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
    public boolean likeTopic(Article article,Integer userID){
        if(userID==0){return false;}       //保证 userId 参数存在
        Praise praise=new Praise();
        praise.setType(0);
        praise.setType_id(article.getTopicId());
        praise.setUser_id(userID);
        //若两个操作都完成，则返回 true
        if(articleMapper.likeTopic(article)==1&&praiseMapper.addPraise(praise)==1){
            return true;
        }
        return false;
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

