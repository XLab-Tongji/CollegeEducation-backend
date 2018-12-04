package org.lab409.controller;
/**
 * Created by jiao on 2018/11/4
 */
import org.lab409.entity.Question;
import org.lab409.entity.ResponseMessage;
import org.lab409.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    QuestionService questionService;

    //get all questions
    @RequestMapping(value = "/question/get/all",method = RequestMethod.GET)
    public ResponseMessage getAllQuestions(){
        List<Question> questions=questionService.getAllQuestions();
        return new ResponseMessage<>(questions).success();
    }

    //save question
    @RequestMapping(value = "/question/save",method = RequestMethod.POST)
    public ResponseMessage saveQuestion(@RequestBody Question question){
        if(questionService.saveQuestion(question)){
            return new ResponseMessage<Question>(null).success();
        }
        return new ResponseMessage<Question>(null).error(202,"save failed");
    }

    //set picture_path of a question
    @RequestMapping(value = "/question/path/set",method = RequestMethod.POST)
    public ResponseMessage setPicturePath(@RequestBody Question question){
        if(questionService.setPicturePath(question)){
            return new ResponseMessage<Question>(null).success();
        }
        return new ResponseMessage<Question>(null).error(202,"error");
    }
}
