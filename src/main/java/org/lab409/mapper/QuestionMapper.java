package org.lab409.mapper;

/*
 ** created by jiao on 2018/12/4
 */

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.lab409.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 ** created by jiao on 2018/12/4
 */

@Mapper
@Repository
public interface QuestionMapper {
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
            "b.sector_name " +
            "FROM forum_question a,forum_question_sector b " +
            "WHERE a.question_sector_ID=b.sector_ID " +
            "ORDER BY a.question_date DESC")
    List<Question> getAllQuestions();
}
