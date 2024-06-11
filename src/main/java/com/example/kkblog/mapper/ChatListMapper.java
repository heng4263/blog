package com.example.kkblog.mapper;

import com.example.kkblog.domain.ChatList;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Repository
public interface ChatListMapper {
    @Select("select c.*,u.avatar from chat_list as c inner join user as u on c.userId = u.id  where  linkId = #{linkId} order by createtime asc")
    List<ChatList> selectChat(int linkId);

    @Insert("insert into chat_list(linkId,userId,content,createtime) values (#{linkId},#{fromId},#{content},#{timestamp});")
    void insertChat(Integer linkId, Integer fromId, String content, Timestamp timestamp);
}