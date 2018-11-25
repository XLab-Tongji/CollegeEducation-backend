package org.lab409.service.impl;

import org.lab409.entity.UserEntity;
import org.lab409.mapper.UserMapper;
import org.lab409.security.JwtTokenUtil;
import org.lab409.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserUtilImpl implements UserUtil {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserEntity getCurrentUser() {
        UserDetails userDetails = getUserHelperInfo();
        String username = userDetails.getUsername();
        return userMapper.getUserByName(username);
    }

    @Override
    public String getCurrentPassword() {
        UserDetails userDetails = getUserHelperInfo();
        String username = userDetails.getUsername();
        return userMapper.getCurrentPassword(username).getPassword();
    }

    @Override
    public UserDetails getUserHelperInfo() {
        return  (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
