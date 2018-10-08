package org.lab409.service.impl;
import org.lab409.entity.MybatisEntity;
import org.lab409.mapper.TestMybatisMapper;
import org.lab409.service.TestMybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TestMybatisServiceImpl1 {
    @Autowired
    private TestMybatisMapper testMybatisMapper1;

    public ArrayList<MybatisEntity> getMybatis(){
        return testMybatisMapper1.getMybatis();
    }
}