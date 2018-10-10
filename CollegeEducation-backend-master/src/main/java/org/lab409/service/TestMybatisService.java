package org.lab409.service;

import org.lab409.entity.MybatisEntity;
import org.lab409.mapper.TestMybatisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by xuawai on 2018/9/25.
 */
public interface TestMybatisService {

    public ArrayList<MybatisEntity> getMybatis();

}
