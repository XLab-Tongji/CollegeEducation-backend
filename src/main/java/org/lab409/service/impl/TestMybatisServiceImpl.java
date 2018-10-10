package org.lab409.service.impl;

import org.lab409.entity.MybatisEntity;
import org.lab409.mapper.TestMybatisMapper;
import org.lab409.service.TestMybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by xuawai on 2018/9/25.
 */
@Service
public class TestMybatisServiceImpl implements TestMybatisService {

    @Autowired
    private TestMybatisMapper testMybatisMapper;
    public ArrayList<MybatisEntity> getMybatis(){

        return testMybatisMapper.getMybatis();
    }


}
