package org.lab409.mapper;

import org.apache.ibatis.annotations.*;
import org.lab409.entity.Clazz;
import org.lab409.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 ** created by jiao on 2018/12/24
 */
@Mapper
@Repository
public interface ClazzMapper {

    @Insert("INSERT INTO " +
            "course_admin (admin_name,admin_class,admin_userid) " +
            "VALUES(#{admin_name},#{course_no},#{admin_userid})")
    int createAssociationBetweenClazzAndTeacher(Clazz clazz);


    @Insert("INSERT INTO course_class " +
            "(course_no," +
            "course_name," +
            "course_term," +
            "course_start_date," +
            "course_signup_link," +
            "course_access_code," +
            "course_class_link," +
            "course_instructor_self_signup," +
            "course_class_status," +
            "course_credit) " +
            "VALUES" +
            "(#{course_no}," +
            "#{course_name}," +
            "#{course_term}," +
            "#{course_start_date}," +
            "#{course_signup_link}," +
            "#{course_access_code}," +
            "#{course_class_link}," +
            "#{course_instructor_self_signup}," +
            "#{course_class_status}," +
            "#{course_credit})")
    @Options(useGeneratedKeys = true,keyProperty = "course_id",keyColumn = "course_id")
    int createClazz(Clazz clazz);

    @Insert("INSERT INTO course_students " +
            "(student_email) " +
            "VALUES" +
            "(#{student_email})")
    @Options(useGeneratedKeys = true,keyProperty = "student_id",keyColumn = "student_id")
    int addStudent(Student student);

    @Insert("INSERT INTO course_score " +
            "(course_no," +
            "student_id) " +
            "VALUES" +
            "(#{course_no}," +
            "#{student_id})")
    int addAssociationBetweenStudentAndClass(@Param(value = "student_id")Integer student_id, @Param(value = "course_no")String course_no);

    @Delete("DELETE FROM course_score WHERE student_id=#{student_id} AND course_no=#{course_no}")
    int deleteAssociationBetweenStudentAndClass(@Param(value = "student_id")Integer student_id,@Param(value = "course_no")String course_no);

    @Select("SELECT IFNULL((SELECT a.course_no FROM course_class a WHERE a.course_access_code=#{course_access_code}),'0')")
    String getCourseNoByCourseAccessCode(@Param(value = "course_access_code") String access_code);

    @Select("SELECT * " +
            "FROM course_admin a,course_class b " +
            "WHERE a.admin_class=b.course_no " +
            "AND a.admin_userid=#{userid}")
    List<Clazz> getClazzAsTeacher(@Param(value = "userid") Integer userid);

    @Select("SELECT * " +
            "FROM course_class a,course_score b,course_students c " +
            "WHERE a.course_no=b.course_no " +
            "AND b.student_id=c.student_id " +
            "AND c.student_email=#{student_email}")
    List<Clazz> getClazzAsStudent(@Param(value = "student_email") String student_email);

    @Select("SELECT IFNULL((SELECT a.student_id FROM course_students a WHERE a.student_email=#{student_email}),'0')")
    int getStudentIdByStudentEmail(@Param(value = "student_email")String student_email);
}
