package org.lab409.service.impl;

import org.lab409.entity.Clazz;
import org.lab409.entity.Student;
import org.lab409.mapper.ClazzMapper;
import org.lab409.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 ** created by jiao on 2018/12/24
 */

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    ClazzMapper clazzMapper;

    @Override
    public boolean createClazz(Clazz clazz){
        if(clazzMapper.createClazz(clazz)!=1)
            return false;
        if(clazzMapper.createAssociationBetweenClazzAndTeacher(clazz)!=1)
            return false;
        return true;
    }

    @Override
    public boolean addStudent(Student student,String course_no){
        if(clazzMapper.addStudent(student)!=1)
            return false;
        if(clazzMapper.addAssociationBetweenStudentAndClass(student.getStudent_id(),course_no)!=1)
            return false;
        return true;
    }

    @Override
    public boolean deleteStudent(Student student,String course_no){
        Integer studentId=clazzMapper.getStudentIdByStudentEmail(student.getStudent_email());
        if(studentId==0)
            return false;
        if(clazzMapper.deleteAssociationBetweenStudentAndClass(studentId,course_no)!=1)
            return false;
        return true;
    }

    @Override
    public boolean enterClazz(Student student,String course_access_code){
        String course_no=clazzMapper.getCourseNoByCourseAccessCode(course_access_code);
        if(course_no.equals("0"))
            return false;
        Integer studentId=clazzMapper.getStudentIdByStudentEmail(student.getStudent_email());
        if(studentId==0){
            clazzMapper.addStudent(student);
            clazzMapper.addAssociationBetweenStudentAndClass(student.getStudent_id(),course_no);
        }
        else{
            if(clazzMapper.addAssociationBetweenStudentAndClass(studentId,course_no)!=1)
                return false;
        }
        return true;
    }

    @Override
    public List<Clazz> getClazzAsTeacher(Integer userid){
        return clazzMapper.getClazzAsTeacher(userid);
    }

    @Override
    public List<Clazz> getClazzAsStudent(String student_email){
        return clazzMapper.getClazzAsStudent(student_email);
    }
}
