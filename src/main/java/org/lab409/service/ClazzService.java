package org.lab409.service;


import org.lab409.entity.Clazz;
import org.lab409.entity.Student;

import java.util.List;

/*
 ** created by jiao on 2018/12/24
 */
public interface ClazzService {
    boolean createClazz(Clazz clazz);                                           //create a class
    boolean addStudent(Student student,String course_no);                       //add students
    boolean deleteStudent(Student student,String course_no);                    //delete association between student and class
    boolean enterClazz(Student student,String course_access_code);              //enter a class by providing student_email and course_access_code
    List<Clazz> getClazzAsTeacher(Integer userid);                              //get class as teacher
    List<Clazz> getClazzAsStudent(String student_email);                        //get class as student
    List<Student> getClazzMemberAsTeacher(String course_no);                    //get class members as teacher
    List<Student> getClazzMemberAsStudent(String course_no);                    //get class members as student
}
