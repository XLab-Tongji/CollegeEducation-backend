package org.lab409.controller;

import java.io.IOException;
import java.util.Map;
import org.lab409.service.ResourceService;
import org.lab409.service.UserService;
import org.lab409.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping(path = "/user/logout", method = RequestMethod.POST)
    public ResponseEntity userLogOutController() {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
        userService.userLogOut();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/user/uploadIcon", method = RequestMethod.PUT)
    @CrossOrigin
    public ResponseEntity uploadIcon(@RequestParam(name = "icon") MultipartFile icon) {
        String success = userService.uploadIcon(icon);
        if(success.equals(ResourceService.OK)) {
            return ResponseEntity.status(200).body(new ResponseMessage<>(null).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, success));
    }

    @RequestMapping(path = "/user/getIcon",method = RequestMethod.GET)
    public ResponseEntity getIcon() {
        byte[] icon = userService.getIcon();
        if(icon == null) {
            return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "no icon"));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "icon");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(ResourceService.tika.detect(icon)))
                .contentLength(icon.length)
                .headers(headers)
                .body(icon);
    }
}
