package com.example.kkblog.controller;

import com.example.kkblog.domain.User;
import com.example.kkblog.room.OnlineUserManager;
import com.example.kkblog.room.RequestMessage;
import com.example.kkblog.room.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collection;

@Component
public class RoomController extends TextWebSocketHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OnlineUserManager onlineUserManager;

    // 连接成功调用
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = (User) session.getAttributes().get("user");
        onlineUserManager.enterHall(user.getId(),session);
        System.out.println("当前人数: " + onlineUserManager.getOnlinePeople());
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("people");
        responseMessage.setNumber(onlineUserManager.getOnlinePeople());
        Collection<WebSocketSession> collection = onlineUserManager.getAllSession();
        for(WebSocketSession s : collection) {
            s.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(responseMessage)));
        }
    }

    // 连接成功收到的响应
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        User user = (User) session.getAttributes().get("user");
        String payload = message.getPayload();
        System.out.println(payload);
        RequestMessage requestMessage = objectMapper.readValue(payload,RequestMessage.class);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("chatMessage");
        responseMessage.setContent(requestMessage.getContent());
        Collection<WebSocketSession> collection = onlineUserManager.getAllSession();
        for(WebSocketSession s : collection) {
            s.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(responseMessage)));
        }
    }

    // 连接异常调用
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        User user = (User) session.getAttributes().get("user");
        WebSocketSession webSocketSession = onlineUserManager.getState(user.getId());
        if(webSocketSession == session) {
            // 2. 设置在线状态
            onlineUserManager.exitHall(user.getId());
        }
        System.out.println("用户"+user.getUsername()+"退出");
    }

    // 连接关闭调用
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = (User) session.getAttributes().get("user");
        WebSocketSession webSocketSession = onlineUserManager.getState(user.getId());
        if(webSocketSession == session) {
            // 2. 设置在线状态
            onlineUserManager.exitHall(user.getId());
        }
        System.out.println("用户"+user.getUsername()+"退出");
    }
}