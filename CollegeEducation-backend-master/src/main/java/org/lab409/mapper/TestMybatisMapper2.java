package org.lab409.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.lab409.entity.MybatisEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Mapper
@Component
public interface TestMybatisMapper2 {

    @Select("select * from education_major ")

    ArrayList<MybatisEntity> getMybatis();
}
