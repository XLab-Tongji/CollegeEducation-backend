package org.lab409.service;

import org.lab409.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.AbstractMap;
import java.util.Map;

public interface UserService {
    String insertUser(UserEntity user);
    String updateUserInfo(UserEntity user);
    String updateUserPassword(Map<String,String> passwords);
    void userLogOut();
    String uploadIcon(MultipartFile icon);
    byte[] getIcon();
}
