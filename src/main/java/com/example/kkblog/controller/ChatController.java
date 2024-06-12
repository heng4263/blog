package com.example.kkblog.controller;


import com.example.kkblog.domain.ChatList;
import com.example.kkblog.domain.Message;
import com.example.kkblog.domain.User;
import com.example.kkblog.domain.dto.UserDto;
import com.example.kkblog.mapper.UserMapper;
import com.example.kkblog.room.OnlineUserManager;
import com.example.kkblog.room.RequestMessage;
import com.example.kkblog.room.ResponseMessage;
import com.example.kkblog.service.ChatListService;
import com.example.kkblog.service.UserLinkService;
import com.example.kkblog.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.print.attribute.standard.OrientationRequested;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Transactional
public class ChatController extends TextWebSocketHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChatListService chatListService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OnlineUserManager onlineUserManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLinkService userLinkService;

    // 连接成功调用
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage();
        // 1. 首先判断当前用户是否已经登录, 防止用户多开
        UserDto user = (UserDto) session.getAttributes().get("user");
        if(onlineUserManager.getState(user.getId()) != null) {
            responseMessage.setStatus(-1);
            responseMessage.setMessage("当前用户已经登录了, 不要重复登录");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(responseMessage)));
            return;
        }
        // 2. 将用户的在线状态设置为在线
        onlineUserManager.enterHall(user.getId(),session);
        // 3. 从数据库中查找所有的用户
        // 4. 设置响应类, 并添加对应的信息
        responseMessage.setStatus(1);
        responseMessage.setMessage("getUser");
        responseMessage.setFromusername(user.getUsername());
        responseMessage.setUsers(userService.selectAllUser(user.getUsername()));
        // 5. 返回响应
        session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(responseMessage)));
    }

    // 连接成功收到的响应
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage();
        UserDto user = (UserDto) session.getAttributes().get("user");
        // 1. 解析请求的内容
        String payload = message.getPayload();
        System.out.println(payload);
        RequestMessage requestMessage = objectMapper.readValue(payload,RequestMessage.class);
        // 2. 判断是加载消息记录, 还是发送消息
        if(requestMessage.getMessage().equals("loadMessage")){
            // 2.a.1 根据两者的用户Id 查看 linkId, 这里让from始终最小,to始终最大,方便查找
            responseMessage.setMessage("loadMessage");
            responseMessage.setTousername(requestMessage.getTo());
            Integer fromId = userService.selectUserId(requestMessage.getFrom());
            Integer toId = userService.selectUserId(requestMessage.getTo());
            int min = Math.min(fromId,toId);
            int max = Math.max(fromId,toId);
            Integer linkId = userLinkService.selectLinkId(min,max);
            // 2.a.2 判断当前的linkId是否存在, 不存在就不需要加载聊天记录了
            if(linkId == null || linkId == 0) {
                responseMessage.setStatus(1);
                responseMessage.setMessages(null);
            }else{
                // 2.a.3 存在聊天记录,需要加载,
                responseMessage.setStatus(1);
                List<ChatList> chatLists = chatListService.selectChat(linkId);
                List<Message> messages = new ArrayList<>();
                for(ChatList chatList : chatLists) {
                    Message message1 = new Message();
                    message1.setMessage(chatList.getContent());
                    message1.setUserId(chatList.getUserId());
                    message1.setAvatar(chatList.getAvatar());
                    message1.setSender(chatList.getUserId() == user.getId());
                    messages.add(message1);
                }
                responseMessage.setMessages(messages);
            }
            // 2.a.4 设置对应的响应, 并返回
            session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(responseMessage)));
        }
        if(requestMessage.getMessage().equals("sendMessage")) {
            // 2.b.1 查找对应的linkId
            Integer fromId = userService.selectUserId(requestMessage.getFrom());
            Integer toId = userService.selectUserId(requestMessage.getTo());
            int min = Math.min(fromId,toId);
            int max = Math.max(fromId,toId);
            Integer linkId = userLinkService.selectLinkId(min,max);
            // 2.b.2 判断当前linkId是否为空, 为空需要创建linkId
            if(linkId == null || linkId == 0) {
                responseMessage.setStatus(1);
                userLinkService.insertLink(min,max);
                linkId = userLinkService.selectLinkId(min,max);
            }
            // 2.b.3 根据linkId, 在聊天列表中添加数据
            String content = requestMessage.getContent();
            chatListService.insertChat(linkId,fromId,content,new Timestamp(System.currentTimeMillis()));
            // 2.b.4 设置对应的响应信息
            responseMessage.setStatus(1);
            responseMessage.setMessage("sendMessage");
            Message message1 = new Message();
            // 2.b.5 获取两者用户的session, 并判断是否在线, 给在线的用户返回响应, 刷新聊天框
            WebSocketSession session1 = onlineUserManager.getState(fromId);
            WebSocketSession session2 = onlineUserManager.getState(toId);
            if(session1 != null) {
                message1.setSender(true);
                message1.setUserId(fromId);
                message1.setAvatar(userMapper.selectUserDtoByUserId(fromId).getAvatar());
                message1.setMessage(content);
                List<Message> list = new ArrayList<>();
                list.add(message1);
                responseMessage.setMessages(list);
                session1.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(responseMessage)));
            }
            if(session2 != null) {
                message1.setSender(false);
                message1.setUserId(toId);
//                message1.setAvatar(userMapper.selectUserDtoByUserId(toId).getAvatar());
                message1.setAvatar(userMapper.selectUserDtoByUserId(fromId).getAvatar());

                message1.setMessage(content);
                List<Message> list = new ArrayList<>();
                list.add(message1);
                responseMessage.setMessages(list);
                session2.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(responseMessage)));
            }
        }
    }

    // 连接异常调用
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        UserDto user = (UserDto) session.getAttributes().get("user");
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
        UserDto user = (UserDto) session.getAttributes().get("user");
        WebSocketSession webSocketSession = onlineUserManager.getState(user.getId());
        if(webSocketSession == session) {
            // 2. 设置在线状态
            onlineUserManager.exitHall(user.getId());
        }
        System.out.println("用户"+user.getUsername()+"退出");
    }
}
