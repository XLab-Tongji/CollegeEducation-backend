package org.lab409.service.impl;

import org.lab409.entity.UserEntity;
import org.lab409.mapper.UserMapper;
import org.lab409.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean insertUser(UserEntity user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if(userMapper.insertUser(user)!= 1) {
            return false;
        }
        if(userMapper.insertUserAuth(user.getUserID(),user.getAuth() )!= 1) {
            return false;
        }
        return true;
    }
}
