package org.lab409.controller;

import org.lab409.service.UserService;
import org.lab409.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public ResponseMessage registerController(@RequestBody UserEntity user) {
        if(userService.insertUser(user)) {
            return new ResponseMessage<UserEntity>(null).success();
        }
        return new ResponseMessage<UserEntity>(null).error(202,"can't register");
    }
}
