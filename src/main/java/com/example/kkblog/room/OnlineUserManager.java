package com.example.kkblog.room;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUserManager {
    // 哈希表存储的是用户的当前的状态,在线就存储到哈希表中
    private ConcurrentHashMap<Integer, WebSocketSession> userState = new ConcurrentHashMap<>();

    public void enterHall(int userId, WebSocketSession webSocketSession) {
        userState.put(userId,webSocketSession);
    }

    public void exitHall(int userId) {
        userState.remove(userId);
    }

    public WebSocketSession getState(int userId) {
        return userState.get(userId);
    }

    public int getOnlinePeople() {
        return userState.size();
    }

    public Collection<WebSocketSession> getAllSession() {
        return userState.values();
    }
}