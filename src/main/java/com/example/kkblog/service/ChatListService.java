package com.example.kkblog.service;

import com.example.kkblog.domain.ChatList;
import com.example.kkblog.mapper.ChatListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ChatListService {
    @Autowired
    private ChatListMapper chatListMapper;

    public List<ChatList> selectChat(int linkId){
        return chatListMapper.selectChat(linkId);
    }

    public void insertChat(Integer linkId, Integer fromId, String content, Timestamp timestamp) {
        chatListMapper.insertChat(linkId,fromId,content,timestamp);
    }
}