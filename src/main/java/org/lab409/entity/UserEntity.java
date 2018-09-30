package org.lab409.entity;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class UserEntity {

    private Integer userID;
    private String username;
    private String password;
    private String gender;
    private String email;
    private Integer universityID;
    private String majorID;
    private Integer auth;
    private String birthday;
    private String studentID;
    private String admissionYear;

    public UserEntity encode() {
        this.password = new BCryptPasswordEncoder().encode(this.password);
        return this;
    }
}
