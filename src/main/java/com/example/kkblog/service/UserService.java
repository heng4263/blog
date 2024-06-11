package com.example.kkblog.service;

import com.example.kkblog.domain.User;
import com.example.kkblog.domain.dto.UserDto;
import com.example.kkblog.mapper.UserMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;


    public User selectByUserName(String username) {
        return userMapper.selectByUserName(username);
    }

    public void addUser(User user) {
        userMapper.addUser(user);
    }

    public List<User> selectAllUser(String username){
        return userMapper.selectAllUser(username);
    }

    public Integer selectUserId(String username) {
        return userMapper.selectUserId(username);
    }


}
