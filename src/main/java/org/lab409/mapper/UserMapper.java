package org.lab409.mapper;

import org.apache.ibatis.annotations.*;
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

    @Select("SELECT ID, USERNAME, EMAIL, ADMISSIONYEAR, STUDENTID, GENDER, BIRTHDAY, UNIVERSITYID, MAJORID, POINTS " +
            "FROM USER WHERE USERNAME = #{username}")
    @Results({@Result(column = "ID", property = "userID"),
            @Result(column = "USERNAME", property = "username"),
            @Result(column = "EMAIL", property = "email"),
            @Result(column = "ADMISSIONYEAR", property = "admissionYear"),
            @Result(column = "STUDENTID", property = "studentID"),
            @Result(column = "GENDER", property = "gender"),
            @Result(column = "BIRTHDAY", property = "birthday"),
            @Result(column = "universityID", property = "universityID"),
            @Result(column = "MAJORID", property = "majorID"),
            @Result(column = "POINTS", property = "points")})
    UserEntity getUserByName(@Param("username") String username);
}
