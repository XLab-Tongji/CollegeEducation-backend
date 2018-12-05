package org.lab409.service;

import org.lab409.entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 ** created by jiao on 2018/12/4
 */
public interface QuestionService {
    List<Question> getAllQuestions();                //get all questions
    boolean saveQuestion(Question question);         //save question
    boolean setPicturePath(Question question);       //set picture_path of a question
}
