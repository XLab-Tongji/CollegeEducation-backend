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

public class TestAuthController2 {
    @Autowired
    private TestMybatisService testMybatisService2;

    @RequestMapping(path = "/test/major", method = RequestMethod.GET)
    @PreAuthorize("hasRole('MAJOR')")
    public String getMajor(){
        return "major";
    }

    @RequestMapping(path = "/test/mybatis", method = RequestMethod.GET)
    @PreAuthorize("hasRole('MAJOR')")
    public ArrayList<MybatisEntity> testMybatis(){
        return testMybatisService2.getMybatis();
    }
}

*/
