package org.lab409.service;

import org.lab409.entity.*;
import java.util.Map;

public interface UserService {
    String insertUser(UserEntity user);
    String updateUserInfo(UserEntity user);
    String updateUserPassword(Map<String,String> passwords);
    void userLogOut();
}
