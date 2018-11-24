package org.lab409.service.impl;

import org.apache.tika.Tika;
import org.lab409.entity.UserEntity;
import org.lab409.mapper.UserMapper;
import org.lab409.security.JwtTokenUtil;
import org.lab409.service.ResourceService;
import org.lab409.service.UserService;
import org.lab409.util.MimeTypes;
import org.lab409.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserUtil userUtil;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //dataurl unfinished
    @Override
    @Transactional
    public String insertUser(UserEntity user) {
        /*byte[] bytes = Base64.getUrlDecoder().decode(user.getIcon());
        String type = ResourceService.tika.detect(bytes);
        if (!MimeTypes.isIconValid(type)) {
            return "icon type wrong";
        }*/
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if(userMapper.insertUser(user)!= 1) {
            return "user register fail";
        }
        if(userMapper.insertUserAuth(user.getUserID(),user.getAuth() )!= 1) {
            return "user register fail";
        }
        return ResourceService.OK;
    }

    //dataurl handler needed
    // unfinished
    @Override
    @Transactional
    public String updateUserInfo(UserEntity user) {
        /*byte[] bytes = Base64.getUrlDecoder().decode(user.getIcon());
        String type = ResourceService.tika.detect(bytes);
        if (!MimeTypes.isIconValid(type)) {
            return "icon type wrong";
        }*/
        user.setUserID(userUtil.getCurrentUser().getUserID());
        return userMapper.updateUserInfo(user) == 1? ResourceService.OK:"update info fail";
    }

    @Override
    @Transactional
    public String updateUserPassword(Map<String, String> passwords) {
        UserDetails userDetails = userUtil.getUserHelperInfo();
        if (!bCryptPasswordEncoder.matches(passwords.get("oldPassword"),userDetails.getPassword() )) {
            return "old password wrong";
        }
        return userMapper.setPassword(bCryptPasswordEncoder.encode(passwords.get("newPassword")), userDetails.getUsername()) == 1? ResourceService.OK:"change password fail";
    }

    //好像不该这么写。。。暂时不懂jwt怎么做
    @Override
    public void userLogOut() {
        UserDetails userDetails = userUtil.getUserHelperInfo();
        //new SecurityContextLogoutHandler().l
        jwtTokenUtil.generateToken(userDetails);
    }

    @Override
    public String uploadIcon(MultipartFile icon) {
        try {
            byte[] iconBytes = icon.getBytes();
            String type = ResourceService.tika.detect(iconBytes);
            if (!MimeTypes.isIconValid(type)) {
                return "icon type wrong";
            }
            String dataURL = Base64.getUrlEncoder().encodeToString(iconBytes);
            return userMapper.uploadIcon(dataURL, userUtil.getCurrentUser().getUserID()) == 1?
            ResourceService.OK:"upload icon fail";
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "upload icon fail";
    }

    @Override
    public byte[] getIcon() {
        String dataURL = userMapper.getIcon(userUtil.getCurrentUser().getUserID()).getIcon();
        if(dataURL == null) {
            return null;
        }
        return Base64.getUrlDecoder().decode(dataURL);
    }
}
