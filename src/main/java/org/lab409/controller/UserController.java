package org.lab409.controller;

import java.util.Map;
import org.lab409.service.ResourceService;
import org.lab409.service.UserService;
import org.lab409.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public ResponseEntity registerController(@RequestBody UserEntity user) {
        String success = userService.insertUser(user);
        if(success.equals(ResourceService.OK)) {
            return ResponseEntity.status(200).body(new ResponseMessage<>(null).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202,success));
    }

    @RequestMapping(path = "/user/update", method = RequestMethod.PUT)
    public ResponseEntity updateUserInfoController(@RequestBody UserEntity user) {
        String success = userService.updateUserInfo(user);
        if (success.equals(ResourceService.OK)) {
            return ResponseEntity.status(200).body(new ResponseMessage<>(null).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202,success));
    }

    @RequestMapping(path = "/user/updatePassword", method = RequestMethod.PUT)
    public ResponseEntity updateUserPasswordController(@RequestBody Map<String,String> passwords) {
        String success = userService.updateUserPassword(passwords);
        if(success.equals(ResourceService.OK)) {
            return ResponseEntity.status(200).body(new ResponseMessage<>(null).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, success));
    }

    @RequestMapping(path = "/userLogout", method = RequestMethod.POST)
    public ResponseEntity userLogOutController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
        userService.userLogOut();
        return ResponseEntity.ok().build();
    }
}
