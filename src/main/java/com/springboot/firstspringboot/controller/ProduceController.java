package com.springboot.firstspringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Dbg
 * @create 2019/8/15 11:05
 */
@Controller
public class ProduceController {

    //注入JmsTemplate
    @Autowired
    private JmsTemplate jmsTemplate;

    @RequestMapping("/register")
    public String register(HttpServletRequest request) throws MessagingException {
        jmsTemplate.send("mail_lp", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("title","注册邮件");
                message.setString("email","it379862818@163.com");
                return message;
            }
        });
        return "/success";
    }
}
