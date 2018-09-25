package org.lab409.mapper;

/**
 * Created by xuawai on 2018/9/25.
 */
import org.apache.ibatis.annotations.*;
import org.lab409.entity.MybatisEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Mapper
@Component
public interface TestMybatisMapper {

    @Select("select * from mybatis")
    ArrayList<MybatisEntity> getMybatis();
}
