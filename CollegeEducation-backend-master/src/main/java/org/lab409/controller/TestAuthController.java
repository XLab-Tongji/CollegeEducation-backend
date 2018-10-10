package org.lab409.controller;

import org.lab409.entity.MybatisEntity;
import org.lab409.service.TestMybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestAuthController {

    @Autowired
    private TestMybatisService testMybatisService;

    @RequestMapping(path = "/test/student", method = RequestMethod.GET)
    @PreAuthorize("hasRole('STUDENT')")
    public String getStudent() {
        return "student";
    }


    @RequestMapping(path = "/test/teacher", method = RequestMethod.GET)
    @PreAuthorize("hasRole('TEACHER')")
    public String getTeacher() {
        return "teacher";
    }

    @RequestMapping(path = "/test/mybatis", method = RequestMethod.GET)
    @PreAuthorize("hasRole('STUDENT')")
    public ArrayList<MybatisEntity> testMybatis() {
        return testMybatisService.getMybatis();
    }
}
