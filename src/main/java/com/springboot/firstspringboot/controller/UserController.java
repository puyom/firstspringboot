package com.springboot.firstspringboot.controller;


import com.springboot.firstspringboot.common.PageResult;
import com.springboot.firstspringboot.entity.User;
import com.springboot.firstspringboot.service.UserService;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @RequestMapping("/findAll/{page}/{size}")
//    page：当前页数   size：当前页需要显示的数量
    public PageResult<User> findUsers(HttpServletRequest request, @PathVariable("page") int page,
                                @PathVariable("size") int size){
        PageResult<User> all = userService.findAll(page, size);
        log.info("=======================分页查看数据第"+page+"页========================");
        return all;
    }

    @RequestMapping("/setKey")
    public String setKey(){
        // 保存字符串
        stringRedisTemplate.opsForValue().set("aaa", "111");
        return (String) stringRedisTemplate.opsForValue().get("aaa");
    }


    @Test
    public void test01(){
        User user=User.builder()
                .country("1")
                .email("3798762818@qq.com")
                .build();
        log.info(user.toString());
    }



}
