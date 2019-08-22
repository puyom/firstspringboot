package com.springboot.firstspringboot.service;



import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.springboot.firstspringboot.common.PageResult;
import com.springboot.firstspringboot.entity.User;
import com.springboot.firstspringboot.entity.UserExample;
import com.springboot.firstspringboot.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public PageResult<User> findAll(int page, int size){
        PageHelper.startPage(page,size);
        Page<User> pageUser = (Page<User>) userMapper.selectByExample(null);
        return new PageResult<>(pageUser.getTotal(),pageUser.getResult());

    }
}
