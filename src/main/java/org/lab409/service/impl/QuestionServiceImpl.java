package org.lab409.service.impl;

import org.lab409.entity.Question;
import org.lab409.mapper.QuestionMapper;
import org.lab409.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 ** created by jiao on 2018/12/4
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Override
    public List<Question> getAllQuestions(){
        return questionMapper.getAllQuestions();
    }
}
