package org.lab409.controller;
/**
 * Created by jiao on 2018/11/4
 */
import org.lab409.entity.Question;
import org.lab409.entity.ResponseMessage;
import org.lab409.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
