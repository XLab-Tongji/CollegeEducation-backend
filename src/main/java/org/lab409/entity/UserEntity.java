package org.lab409.entity;

import lombok.Data;

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
    private Integer points = 100;

    //data url base64
    private String icon;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
