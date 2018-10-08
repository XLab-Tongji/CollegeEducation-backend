/*

package org.lab409.controller;
import org.lab409.entity.MybatisEntity;
import org.lab409.service.TestMybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.Request;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestAuthController1 {
    @Autowired
    private TestMybatisService testMybatisService1;

    @RequestMapping(path = "/test/school", method = RequestMethod.GET)
    @PreAuthorize("hasRole('SCHOOL')")
    public String getSchool(){
        return "school";
    }

    @RequestMapping(path = "/test/mybatis", method = RequestMethod.GET)
    @PreAuthorize("hasRole('SCHOOL')")
    public ArrayList<MybatisEntity> testMybatis1() {return testMybatisService1.getMybatis();
    }
}

*/
