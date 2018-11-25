package org.lab409.controller;

import org.lab409.entity.Blackboard;
import org.lab409.entity.Favorite;
import org.lab409.entity.Reply;
import org.lab409.entity.ResponseMessage;
import org.lab409.service.BlackboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
** created by jiao on 2018/10/26
 */
@RestController
public class BlackboardController {

    @Autowired
    BlackboardService blackboardService;

    //向 forum_blackboard 表中添加数据（存黑板报）
    @RequestMapping(path="blackboard/save",method = RequestMethod.POST)
    public ResponseMessage saveBlackboard(@RequestBody Blackboard blackboard){
            if(blackboardService.saveBlackboard(blackboard)) {
                return new ResponseMessage<Blackboard>(null).success();
            }
        return new ResponseMessage<Blackboard>(null).error(202,"can't save");
    }


    //get blackboards from 'forum_blackboard' table
    @RequestMapping(path = "blackboard/get",method = RequestMethod.GET)
    public ResponseMessage getBlackboard(@RequestParam(value = "SectorName",required = false) String[] SectorName,String SectorState,@RequestParam(value = "userID", defaultValue = "0") Integer userID,@RequestParam(value = "SectorId", defaultValue = "0") Integer SectorId, @RequestParam(value = "keywords",required = false)String keywords){
        List<Blackboard> blackboards=blackboardService.getBlackboard(SectorName,SectorState,userID,SectorId,keywords);
        return new ResponseMessage<>(blackboards).success();
    }


    //praise blackboard
    @Transactional
    @RequestMapping(path = "blackboard/praise",method = RequestMethod.POST)
    public ResponseMessage praiseBlackboard(@RequestBody Blackboard blackboard,@RequestParam(value = "userID",defaultValue = "0")Integer userID){
        if(blackboardService.praiseBlackboard(blackboard,userID)){
            return new ResponseMessage<Blackboard>(null).success();
        }
        return new ResponseMessage<Blackboard>(null).error(202,"praise failed");
    }

    //delete praise of blackboard
    @Transactional
    @RequestMapping(path = "blackboard/praise/delete",method = RequestMethod.POST)
    public ResponseMessage deletePraise(@RequestBody Blackboard blackboard,@RequestParam(value = "userID",defaultValue = "0")Integer userID){
        if(blackboardService.deletePraise(blackboard,userID)){
            return new ResponseMessage<Blackboard>(null).success();
        }
        return new ResponseMessage<Blackboard>(null).error(202,"delete error");
    }

    //collect blackboard
    @Transactional
    @RequestMapping(path = "blackboard/collect",method = RequestMethod.POST)
    public ResponseMessage collectBlackboard(@RequestBody Favorite favorite){
        if(blackboardService.collectBlackboard(favorite)){
            return new ResponseMessage<Blackboard>(null).success();
        }
        return new ResponseMessage<Blackboard>(null).error(202,"collection failed");
    }

    //delete collection
    @Transactional
    @RequestMapping(path = "blackboard/collect/delete",method = RequestMethod.POST)
    public ResponseMessage deleteCollection(@RequestBody Favorite favorite){
        if(blackboardService.deleteCollection(favorite)){
            return new ResponseMessage<Blackboard>(null).success();
        }
        return new ResponseMessage<Blackboard>(null).error(202,"delete error");
    }

    //reply blackboard
    @RequestMapping(path = "blackboard/reply",method = RequestMethod.POST)
    public ResponseMessage replyBlackboard(@RequestBody Reply reply){
        if(blackboardService.replyBlackboard(reply)){
            return new ResponseMessage<Reply>(null).success();
        }
        return new ResponseMessage<Reply>(null).error(202,"reply error");
    }

    //get reply of a blackboard
    @RequestMapping(path = "blackboard/reply/get",method = RequestMethod.GET)
    public ResponseMessage getReply(@RequestParam(value = "TopicId") Integer TopicId,@RequestParam(value = "type",defaultValue = "1")Integer type){
        List<Reply> replies=blackboardService.getReply(TopicId,type);
        return new ResponseMessage<>(replies).success();
    }
}