package org.lab409.mapper;

import org.apache.ibatis.annotations.*;
import org.lab409.entity.MajorEntity;
import org.lab409.entity.UniversityEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UniversityMapper {

    @Select("SELECT * FROM education_university")
    @Results({@Result(column = "univid", property = "universityID"),
            @Result(column = "university", property = "universityName")})
    List<UniversityEntity> getUniversities();


    @Select(("SELECT id, major FROM education_major where univid = #{universityID}"))
    @Results({@Result(column = "id", property = "id"),
            @Result(column = "major", property = "majorName")})
    List<MajorEntity> getMajorByUniversity(@Param("universityID") int universityID);
}
