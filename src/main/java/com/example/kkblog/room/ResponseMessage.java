package com.example.kkblog.room;

import com.example.kkblog.domain.Message;
import com.example.kkblog.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class ResponseMessage {
    //状态码
    public int status;
    //消息数量
    public int number;
    //消息
    public String message;
    //消息内容
    public String content;
    //目标用户
    public String tousername;
    //发送用户
    public String fromusername;
    //用户列表
    public List<User> users;
    //消息列表
    public List<Message> messages;
}
