package org.lab409.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.lab409.entity.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Insert("INSERT IGNORE INTO USER(" +
            "USERNAME," +
            "PASSWORD," +
            "GENDER,"+
            "BIRTHDAY," +
            "EMAIL," +
            "UNIVERSITYID," +
            "MAJORID, " +
            "STUDENTID, " +
            "ADMISSIONYEAR) VALUES(" +
            "#{username}," +
            "#{password}," +
            "#{gender},"+
            "#{birthday}," +
            "#{email}," +
            "#{universityID}," +
            "#{majorID}," +
            "#{studentID}," +
            "#{admissionYear})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "userID", keyColumn = "ID")
    int insertUser(UserEntity user);

    @Insert("INSERT INTO USER_AUTHORITY(USER_ID,AUTHORITY_ID) VALUES(#{userID},#{auth})")
    int insertUserAuth(@Param("userID") int userID, @Param("auth") int auth);
}
