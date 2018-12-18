package org.lab409.service.impl;

import org.lab409.entity.*;
import org.lab409.mapper.*;
import org.lab409.service.ArticleService;
import org.lab409.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.binarywang.java.emoji.EmojiConverter;


import java.util.ArrayList;
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
    @Autowired
    SectorMapper sectorMapper;

    @Override
    public List<ArticleOutput> getArticleBySectorAndKeyword(String[] SectorName, String SectorState, Integer userID, Integer SectorId, String keywords) {
        List<Article> articles=articleMapper.getArticleBySectorAndKeyword(SectorName,SectorState,SectorId, userID,keywords);
        //convert Article to ArticleOutput
        int sectorNameCount=0;
        if(SectorName!=null)
        {
            sectorNameCount = SectorName.length;
        }
        return Converter.convertArticleToArticleOutput(articles,sectorNameCount);
    }

    @Override
    public List<Article> getArticle() {
        return articleMapper.getArticle();
    }

    //保存文章
    @Override
    public boolean saveTopic(ArticleOutput articleoutput){
        //emoji表情转换器，避免 Emoji 存储出现问题
        EmojiConverter emojiConverter=EmojiConverter.getInstance();
        //将article中的TopicText转换
        articleoutput.setTopicText(emojiConverter.toHtml(articleoutput.getTopicText()));
        //将 article 存入 forum_topic 表
        articleMapper.saveTopic(articleoutput);
        //将 article 的标签信息存入 forum_sector_use 表
        for(int i=0;i<articleoutput.getSectorName().size();i++) {
            Integer sector_id=sectorMapper.getSectorIdBySectorName(articleoutput.getSectorName().get(i));
            if(sector_id==-1)
            {
                Sector sector=new Sector();
                sector.setSectorName(articleoutput.getSectorName().get(i));
                sectorMapper.addNewSector(sector);
                if (sectorMapper.addAssociation(articleoutput.getTopicId(), sector.getSectorId()) != 1) {
                    return false;
                }
            }
            else {
                if (sectorMapper.addAssociation(articleoutput.getTopicId(), sector_id) != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    //点赞文章
    @Override
    public boolean likeTopic(ArticleOutput articleOutput,Integer userID){
        if(userID==0){return false;}       //保证 userId 参数存在
        Praise praise=new Praise();
        praise.setType(0);
        praise.setType_id(articleOutput.getTopicId());
        praise.setUser_id(userID);
        //若两个操作都完成，则返回 true
        if(articleMapper.likeTopic(articleOutput)==1&&praiseMapper.addPraise(praise)==1){
            return true;
        }
        return false;
    }
    //取消点赞
    @Override
    public boolean deletePraise(ArticleOutput articleOutput,Integer userID){
        if(userID==0){return false;}
        Praise praise=new Praise();
        praise.setType(0);
        praise.setType_id(articleOutput.getTopicId());
        praise.setUser_id(userID);
        //若两个操作都完成，则返回 true
        if(articleMapper.deletePraise(articleOutput)==1&&praiseMapper.deletePraise(praise)==1){
            return true;
        }
        return false;
    }

    //收藏文章
    @Override
    public boolean collectTopic(Favorite favorite){
        if(favoriteMapper.collectTopic(favorite)==1&&articleMapper.collectTopic(favorite)==1){
            return true;
        }
        return false;
    }
    //delete collection
    @Override
    public boolean deleteCollection(Favorite favorite){
        if(favoriteMapper.deleteCollection(favorite)==1&&articleMapper.deleteCollection(favorite)==1){
            return true;
        }
        return false;
    }
    //评论文章
    @Override
    public boolean replyTopic(Reply reply){
        if(replyMapper.replyTopic(reply)==1&&articleMapper.replyTopic(reply)==1){
            return true;
        }
        return false;
    }
    //获取某篇文章的评论
    @Override
    public List<Reply> getReply(Integer TopicId,Integer type){
        return replyMapper.getReply(TopicId,type);
    }
    //browse an article
    @Override
    public boolean browseTopic(ArticleOutput articleOutput){
        if(articleMapper.browseTopic(articleOutput)==1){
            return true;
        }
        return false;
    }

    //get url of user image
    @Override
    public String getUrlOfUserImage(Integer userID){
        return articleMapper.getUrlOfUserImage(userID);
    }

    //update url of user image
    @Override
    public boolean updateUrlOfUserImage(String image,Integer userID){
        if(articleMapper.updateUrlOfUserImage(image,userID)==1){
            return true;
        }
        return false;
    }
}

