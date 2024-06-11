package com.example.kkblog.domain;

import lombok.Data;

import java.security.Timestamp;

@Data
public class ChatList {
    // 聊天列表id
    private int listId;
    // 聊天关系id
    private int linkId;
    // 发送用户id
    private int userId;
    // 用户名头像
    private String avatar;
    // 聊天内容
    private String content;
    // 发送时间
    private Timestamp createtime;
}