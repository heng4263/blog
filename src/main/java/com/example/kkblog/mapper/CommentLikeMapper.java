package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.CommentLike;
import com.example.kkblog.domain.dto.UserDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hyh
 * @date 2024/4/15
 */

@Mapper
@Repository
public interface CommentLikeMapper extends BaseMapper<CommentLike> {
    // 根据用户id_动态id查询记录
    @Select("SELECT COUNT(`id`) FROM `comment_like` WHERE user_and_comment_id=#{userAndCommentId}")
    Integer selectIfUserLike(String userAndCommentId);

    @Delete("DELETE FROM `comment_like` WHERE user_and_comment_id=#{userAndCommentId}")
    Integer deleteByUserCommentId(String userAndCommentId);

    // 根据用户以及动态id查询用户点赞的评论id
    @Select("SELECT comment_id FROM `comment_like` where dynamic_id=#{dynamicId} and user_id=#{userId}")
    List<Integer> selectLikedCommentIds(CommentLike commentLike);
}
