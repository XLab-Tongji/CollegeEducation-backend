package org.lab409.controller;

import org.lab409.entity.Draft;
import org.lab409.entity.ResponseMessage;
import org.lab409.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
 ** created by jiao on 2018/10/26
 */
@RestController
public class DraftController {

    @Autowired
    DraftService draftService;

    //向 forum_draft 中添加数据（存草稿）
    @RequestMapping(path="draft/save",method = RequestMethod.POST)
    public ResponseMessage saveDraft(@RequestBody Draft draft){
        if(draftService.saveDraft(draft)){
            return new ResponseMessage<Draft>(null).success();
        }
        return new ResponseMessage<Draft>(null).error(202,"can't save");
    }
}
