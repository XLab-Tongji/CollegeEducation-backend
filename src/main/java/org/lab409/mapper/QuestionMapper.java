package org.lab409.mapper;

/*
 ** created by jiao on 2018/12/4
 */

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.lab409.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 ** created by jiao on 2018/12/4
 */

@Mapper
@Repository
public interface QuestionMapper {

    //get all questions
    @Select("SELECT " +
            "a.question_ID," +
            "a.question_title," +
            "a.user_ID," +
            "a.question_text," +
            "a.question_date," +
            "a.answer_ID," +
            "a.answer_count," +
            "a.clicking_rate," +
            "a.question_evaluation," +
            "a.question_follow," +
            "a.question_participate," +
            "a.picture_path," +
            "b.sector_name " +
            "FROM forum_question a,forum_question_sector b " +
            "WHERE a.question_sector_ID=b.sector_ID " +
            "ORDER BY a.question_date DESC")
    List<Question> getAllQuestions();

    //save question
    @Insert("INSERT INTO forum_question " +
            "(question_title," +
            "user_ID," +
            "question_sector_ID," +
            "question_text," +
            "question_date," +
            "answer_ID," +
            "answer_count," +
            "clicking_rate," +
            "question_evaluation," +
            "question_follow," +
            "question_participate," +
            "picture_path)" +
            "VALUE " +
            "(#{question_title}," +
            "#{user_ID}," +
            "#{question_sector_ID}," +
            "#{question_text}," +
            "#{question_date}," +
            "#{answer_ID}," +
            "#{answer_count}," +
            "#{clicking_rate}," +
            "#{question_evaluation}," +
            "#{question_follow}," +
            "#{question_participate}," +
            "#{picture_path})")
    int saveQuestion(Question question);

    //set picture_path of a question
    @Update("UPDATE forum_question SET picture_path=#{picture_path} WHERE question_ID=#{question_ID}")
    int setPicturePath(Question question);


}
