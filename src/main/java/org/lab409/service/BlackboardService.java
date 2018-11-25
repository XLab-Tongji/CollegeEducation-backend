package org.lab409.service;

import org.lab409.entity.Blackboard;
import org.lab409.entity.Favorite;
import org.lab409.entity.Reply;

import java.util.List;

/*
 ** created by jiao on 2018/10/26
 */
public interface BlackboardService {
    boolean saveBlackboard(Blackboard blackboard);                  //save blackboard
    List<Blackboard> getBlackboard(String[] SectorName, String SectorState, Integer userID, Integer SectorId, String keywords);//get blackboard
    boolean praiseBlackboard(Blackboard blackboard,Integer userID); //praise blackboard
    boolean deletePraise(Blackboard blackboard,Integer userID);     //delete praise of blackboard
    boolean collectBlackboard(Favorite favorite);                   //collect blackboard
    boolean deleteCollection(Favorite favorite);                    //delete collection of blackboard
    boolean replyBlackboard(Reply reply);                           //reply blackboard
    List<Reply> getReply(Integer TopicId,Integer type);             //get reply of a blackboard
}
