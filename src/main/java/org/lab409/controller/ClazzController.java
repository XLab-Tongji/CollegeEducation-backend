package org.lab409.controller;

import org.lab409.entity.Clazz;
import org.lab409.entity.ResponseMessage;
import org.lab409.entity.Student;
import org.lab409.entity.UserEntity;
import org.lab409.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 ** created by jiao on 2018/12/24
 */
@RestController
public class ClazzController {
    @Autowired
    ClazzService clazzService;

    //create a class
    @Transactional
    @RequestMapping(value = "class",method = RequestMethod.POST)
    public ResponseMessage createClazz(@RequestBody Clazz clazz){
        if(clazzService.createClazz(clazz))
            return new ResponseMessage<>(null).success();
        return new ResponseMessage<>(null).error(202,"fail to create a class");
    }

    //add students
    @Transactional
    @RequestMapping(value = "class/student",method = RequestMethod.POST)
    public ResponseMessage addStudent(@RequestBody Student student, @RequestParam(value = "course_no") String course_no){
        if(clazzService.addStudent(student,course_no))
            return new ResponseMessage<>(null).success();
        return new ResponseMessage<>(null).error(202,"fail to add");
    }

    //delete student
    @RequestMapping(value = "class/student",method = RequestMethod.DELETE)
    public ResponseMessage deleteStudent(@RequestBody Student student, @RequestParam(value = "course_no") String course_no){
        if(clazzService.deleteStudent(student,course_no))
            return new ResponseMessage<>(null).success();
        return new ResponseMessage<>(null).error(202,"fail to delete");
    }

    //get classes as teacher
    @RequestMapping(value = "teacher/class",method = RequestMethod.GET)
    public ResponseMessage getClazzAsTeacher(@RequestParam(value = "userid")Integer userid){
        List<Clazz> clazzes=clazzService.getClazzAsTeacher(userid);
        return new ResponseMessage<>(clazzes).success();
    }

    //get classes as student
    @RequestMapping(value = "student/class",method = RequestMethod.GET)
    public ResponseMessage getClazzAsStudent(@RequestParam(value = "student_email")String student_email){
        List<Clazz> clazzes=clazzService.getClazzAsStudent(student_email);
        return new ResponseMessage<>(clazzes).success();
    }

    //enter a class by providing student_email and course_access_code
    @Transactional
    @RequestMapping(value = "student/class",method = RequestMethod.POST)
    public ResponseMessage enterClazz(@RequestBody UserEntity userEntity, @RequestParam(value = "course_access_code") String course_access_code){
        if(clazzService.enterClazz(userEntity,course_access_code))
            return new ResponseMessage<>(null).success();
        return new ResponseMessage<>(null).error(202,"fail to enter the class");
    }

    //get class members as teacher
    @RequestMapping(value = "teacher/class/member",method = RequestMethod.GET)
    public ResponseMessage getClazzMemberAsTeacher(@RequestParam(value = "course_no")String course_no){
        List<Student> students=clazzService.getClazzMemberAsTeacher(course_no);
        return new ResponseMessage<>(students).success();
    }

    //get class members as student
    @RequestMapping(value = "student/class/member",method = RequestMethod.GET)
    public ResponseMessage getClazzMemberAsStudent(@RequestParam(value = "course_no")String course_no){
        List<Student> students=clazzService.getClazzMemberAsStudent(course_no);
        return new ResponseMessage<>(students).success();
    }

}
