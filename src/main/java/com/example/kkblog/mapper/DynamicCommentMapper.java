package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.DynamicComment;
import com.example.kkblog.domain.dto.DynamicCommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author xiaoke
 * @Date 2024 04 13 11 53
 **/
@Repository
@Mapper
public interface DynamicCommentMapper extends BaseMapper <DynamicComment> {
    // 查询用户的评论数量
    @Select("SELECT COUNT(id) FROM `dynamic_comment` WHERE user_id=#{userId}")
    Integer selectDynamicCommentNumberByUserId(Integer userId);

    // 查询父亲评论
    @Select("SELECT d.*,u.avatar as avatar, u.nickname as nickname  " +
            "FROM `dynamic_comment` d, `user` u  " +
            "where d.user_id=u.id and d.dynamic_id=#{id} and d.parent_id=-1  " +
            "ORDER BY d.id DESC   " +
            "LIMIT 8  ")
    List<DynamicCommentDto> selectParentCommentByDynamicId(Integer id);

    // 查询子评论
    @Select("SELECT c.*,l.avatar as avatar, l.nickname as nickname, r.nickname as replyUserNickname   " +
            "FROM dynamic_comment c, `user` l, `user` r   " +
            "WHERE c.user_id=l.id and c.reply_user_id=r.id and c.parent_id=#{id}   " +
            "ORDER BY c.id DESC   " +
            "LIMIT 8")
    List<DynamicCommentDto> selectChildCommentByParentId(Integer id);

    // 根据评论id查询评论
    @Select("SELECT c.*,l.avatar as avatar, l.nickname as nickname, r.nickname as replyUserNickname   " +
            "FROM dynamic_comment c, `user` l, `user` r   " +
            "WHERE c.user_id=l.id and c.reply_user_id=r.id and c.id=#{id}")
    DynamicCommentDto selectChildByCommentId(Integer id);

    @Select("SELECT d.*,u.avatar as avatar, u.nickname as nickname  " +
            "FROM `dynamic_comment` d, `user` u  " +
            "where d.user_id=u.id and d.id=#{id}")
    DynamicCommentDto selectParentByCommentId(Integer id);

    // 查询更多父亲评论
    @Select("SELECT d.*,u.avatar as avatar, u.nickname as nickname  " +
            "FROM `dynamic_comment` d, `user` u  " +
            "where d.user_id=u.id and d.dynamic_id=#{dynamicId} and d.parent_id=-1  and d.id < #{id} " +
            "ORDER BY d.id DESC   " +
            "LIMIT 8  ")
    List<DynamicCommentDto> selectMoreParentCommentByDynamicId(DynamicComment dynamicComment);

    // 查询更多子评论
    @Select("SELECT c.*,l.avatar as avatar, l.nickname as nickname, r.nickname as replyUserNickname   " +
            "FROM dynamic_comment c, `user` l, `user` r   " +
            "WHERE c.user_id=l.id and c.reply_user_id=r.id and c.parent_id=#{parentId} and c.id < #{id} " +
            "ORDER BY c.id DESC   " +
            "LIMIT 8")
    List<DynamicCommentDto> selectMoreChildCommentByParent(DynamicComment dynamicComment);

    @Update("UPDATE `dynamic_comment` SET `like_number`=`like_number` + 1 WHERE id=#{id}")
    void addLikeById(Integer id);
    @Update("UPDATE `dynamic_comment` SET `like_number`=`like_number` - 1 WHERE id=#{id}")
    void cancelLikeById(Integer id);
}
