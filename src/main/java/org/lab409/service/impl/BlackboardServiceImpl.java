package org.lab409.service.impl;

import com.github.binarywang.java.emoji.EmojiConverter;
import org.lab409.entity.Blackboard;
import org.lab409.entity.Favorite;
import org.lab409.entity.Praise;
import org.lab409.entity.Reply;
import org.lab409.mapper.BlackboardMapper;
import org.lab409.mapper.FavoriteMapper;
import org.lab409.mapper.PraiseMapper;
import org.lab409.mapper.ReplyMapper;
import org.lab409.service.BlackboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 ** created by jiao on 2018/10/26
 */
@Service
public class BlackboardServiceImpl implements BlackboardService {

    @Autowired
    BlackboardMapper blackboardMapper;
    @Autowired
    PraiseMapper praiseMapper;
    @Autowired
    FavoriteMapper favoriteMapper;
    @Autowired
    ReplyMapper replyMapper;

    @Override
    public boolean saveBlackboard(Blackboard blackboard){
        //emoji表情转换器，避免 Emoji 存储出现问题
        EmojiConverter emojiConverter=EmojiConverter.getInstance();
        //将blackboard中的Blackboard_text转换
        blackboard.setBlackboard_text(emojiConverter.toHtml(blackboard.getBlackboard_text()));
        if(blackboardMapper.saveBlackboard(blackboard)!=1)
            return false;
        return true;
    }


    @Override
    public List<Blackboard> getBlackboard(String[] SectorName, String SectorState, Integer userID, Integer SectorId, String keywords){
        List<Blackboard> blackboards=blackboardMapper.getBlackboard(SectorName,SectorState,SectorId, userID,keywords);
        return blackboards;
    }


    //praise blackboard
    @Override
    public boolean praiseBlackboard(Blackboard blackboard,Integer userID){
        if(userID==0){return false;}       //保证 userId 参数存在
        Praise praise=new Praise();
        praise.setType(1);                 // 1 代表点赞黑板报
        praise.setType_id(blackboard.getBlackboard_id());
        praise.setUser_id(userID);
        //若两个操作都完成，则返回 true
        if(blackboardMapper.praiseBlackboard(blackboard)==1&&praiseMapper.addPraise(praise)==1){
            return true;
        }
        return false;
    }

    //delete praise
    @Override
    public boolean deletePraise(Blackboard blackboard,Integer userID){
        if(userID==0){return false;}       //保证 userId 参数存在
        Praise praise=new Praise();
        praise.setType(1);                 // 1 代表点赞黑板报
        praise.setType_id(blackboard.getBlackboard_id());
        praise.setUser_id(userID);
        //若两个操作都完成，则返回 true
        if(blackboardMapper.deletePraise(blackboard)==1&&praiseMapper.deletePraise(praise)==1){
            return true;
        }
        return false;
    }

    //collect blackboard
    @Override
    public boolean collectBlackboard(Favorite favorite){
        if(favoriteMapper.collectTopic(favorite)==1&&blackboardMapper.collectBlackboard(favorite)==1){
            return true;
        }
        return false;
    }

    //delete collection
    @Override
    public boolean deleteCollection(Favorite favorite){
        if(favoriteMapper.deleteCollection(favorite)==1&&blackboardMapper.deleteCollection(favorite)==1){
            return true;
        }
        return false;
    }

    //reply blackboard
    @Override
    public boolean replyBlackboard(Reply reply){
        if(replyMapper.replyTopic(reply)==1&&blackboardMapper.replyTopic(reply)==1){
            return true;
        }
        return false;
    }

    //get reply of a blackboard
    @Override
    public List<Reply> getReply(Integer TopicId,Integer type){
        return replyMapper.getReply(TopicId,type);
    }

    //browse a blackboard(increase clicking_rate)
    @Override
    public boolean browseBlackboard(Blackboard blackboard){
        if(blackboardMapper.browseBlackboard(blackboard)==1){
            return true;
        }
        return false;
    }

}
