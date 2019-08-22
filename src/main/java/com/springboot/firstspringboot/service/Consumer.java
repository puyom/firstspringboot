package com.springboot.firstspringboot.service;

import com.springboot.firstspringboot.common.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * @author Dbg
 * @create 2019/8/15 11:09
 */
    @Service
    public class Consumer {

    @Autowired
    private MailUtils mailUtils;


        @JmsListener(destination = "mail_lp")  /*监听类*/
        public void sendMail(Message message) throws Exception {
            MapMessage mm = (MapMessage) message;
            String title = mm.getString("title");
            String email = mm.getString("email");
            int code = mailUtils.getCode();
            Session session = mailUtils.getSession();
            MimeMessage imageMail = mailUtils.createImageMail1(session, email, code);
            String code1 = mailUtils.sendEmail(imageMail);
            if("1".equals(code1)){
                //发送成功
                System.out.println("发送邮件成功=======================================");
            }
        }
    }

