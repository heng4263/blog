package com.example.kkblog.domain;

import lombok.Data;

@Data
public class Message {
    // 用户id
    public int userId;
    // 消息
    public String message;
    // 是否是发送者
    public boolean isSender;
    //用户头像
    public String avatar;
}
