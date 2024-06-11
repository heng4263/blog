package com.example.kkblog.room;

import lombok.Data;

@Data
public class RequestMessage {
    private String message;
    private String from;
    private String to;
    private String content;
    private String username;
}
