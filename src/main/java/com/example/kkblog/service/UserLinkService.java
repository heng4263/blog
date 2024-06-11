package com.example.kkblog.service;

import com.example.kkblog.mapper.UserLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLinkService {
    @Autowired
    private UserLinkMapper userLinkMapper;

    public Integer selectLinkId(int fromId,int toId){
        return userLinkMapper.selectLinkId(fromId,toId);
    }

    public void insertLink(int min, int max) {
        userLinkMapper.insertLink(min,max);
    }
}