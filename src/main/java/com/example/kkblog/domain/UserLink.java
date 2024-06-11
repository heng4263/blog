package com.example.kkblog.domain;

import lombok.Data;

@Data
public class UserLink {
    //关系id
    private int linkId;
    //发送用户id
    private int from;
    //接受用户id
    private int to;
}