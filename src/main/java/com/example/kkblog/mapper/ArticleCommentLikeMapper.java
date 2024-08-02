package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.ArticleCommentLike;
import com.example.kkblog.domain.CommentLike;
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
public interface ArticleCommentLikeMapper extends BaseMapper<ArticleCommentLike> {
    // 根据用户id_动态id查询记录
    @Select("SELECT COUNT(`id`) FROM `article_comment_like` WHERE id=#{id} and user_id=#{userId}")
    Integer selectIfUserLikeComment(ArticleCommentLike articleCommentLike);

    @Delete("DELETE FROM `article_comment_like` WHERE id=#{id} and user_id=#{userId}")
    Integer deleteByUserCommentId(ArticleCommentLike articleCommentLike);

    // 根据用户以及动态id查询用户点赞的评论id
    @Select("SELECT comment_id FROM `article_comment_like` where article_id=#{articleId} and user_id=#{userId}")
    List<Integer> selectLikedCommentIds(ArticleCommentLike articleCommentLike);
}
