package org.lab409.controller;

import org.lab409.entity.Blackboard;
import org.lab409.entity.ResponseMessage;
import org.lab409.service.BlackboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
