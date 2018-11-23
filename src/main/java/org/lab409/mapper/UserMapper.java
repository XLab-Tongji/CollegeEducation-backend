package org.lab409.mapper;


import java.util.Map;
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
            "STUDENTID," +
            "ADMISSIONYEAR," +
            "ICON) VALUES(" +
            "#{username}," +
            "#{password}," +
            "#{gender},"+
            "#{birthday}," +
            "#{email}," +
            "#{universityID}," +
            "#{majorID}," +
            "#{studentID}," +
            "#{admissionYear}," +
            "#{icon})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "userID", keyColumn = "ID")
    int insertUser(UserEntity user);

    @Update("UPDATE USER SET " +
            "USERNAME = #{username}, " +
            "EMAIL = #{email}, " +
            "ADMISSIONYEAR = # " +
            "{admissionYear}," +
            "UNIVERSITYID = #{universityID}," +
            " MAJORID = #{majorID}, " +
            "BIRTHDAY = #{birthday}," +
            "GENDER = #{gender}," +
            "STUDENTID = #{studentID}, " +
            "ICON = #{icon} WHERE ID = #{userID}")
    int updateUserInfo(UserEntity user);

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

    @Select("SELECT PASSWORD FROM USER WHERE USERNAME = #{username}")
    @Results({@Result(column = "PASSWORD", property = "password")})
    UserEntity getCurrentPassword(@Param("username") String username);

    @Update("UPDATE USER SET PASSWORD = #{password} WHERE USERNAME = #{username}")
    int setPassword(@Param("password") String password, @Param("username") String username);

    @Select("SELECT USERNAME FROM USER WHERE ID = #{userID}")
    @Results({@Result(column = "USERNAME", property = "username")})
    UserEntity getUserNameByID(@Param("userID") Integer userID);
}
