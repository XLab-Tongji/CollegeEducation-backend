package org.lab409.service.impl;

import org.lab409.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/*
 ** created by jiao on 2018/12/23
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void sendSimpleEmail(String receiver){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("TJSSElab409@163.com");
        message.setTo(receiver);
        message.setSubject("教育平台：注册班级成功");
        message.setText("欢迎来到教育平台，恭喜您创建班级成功！");

        mailSender.send(message);
    }
}
