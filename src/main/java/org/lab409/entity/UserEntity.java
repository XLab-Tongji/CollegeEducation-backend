package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
public class UserEntity {

    private Integer userID;
    private String username;
    private String password;
    private String gender;
    private String email;
    private Integer universityID;
    private Integer majorID;
    private Integer auth;
    private String birthday;
    private String studentID;
    private String admissionYear;

    public UserEntity encode() {
        this.password = new BCryptPasswordEncoder().encode(this.password);
        return this;
    }
}
