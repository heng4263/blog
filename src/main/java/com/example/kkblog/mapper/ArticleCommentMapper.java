package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.ArticleComment;
import com.example.kkblog.domain.dto.ArticleCommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Hyh
 * @Date 2024 04 13 11 53
 **/
@Repository
@Mapper
public interface ArticleCommentMapper extends BaseMapper <ArticleComment> {
    // 查询用户的评论数量
    @Select("SELECT COUNT(id) FROM `article_comment` WHERE user_id=#{userId}")
    Integer selectCommentNumberByUserId(Integer userId);

    // 查询父亲评论
    @Select("SELECT d.*,u.avatar as avatar, u.nickname as nickname  " +
            "FROM `article_comment` d, `user` u  " +
            "where d.user_id=u.id and d.article_id=#{id} and d.parent_id=-1  " +
            "ORDER BY d.id DESC   " +
            "LIMIT 8  ")
    List<ArticleCommentDto> selectParentCommentByArticleId(Integer id);

    // 查询子评论
    @Select("SELECT c.*,l.avatar as avatar, l.nickname as nickname, r.nickname as replyUserNickname   " +
            "FROM article_comment c, `user` l, `user` r   " +
            "WHERE c.user_id=l.id and c.reply_user_id=r.id and c.parent_id=#{id}   " +
            "ORDER BY c.id DESC   " +
            "LIMIT 8")
    List<ArticleCommentDto> selectChildCommentByParentId(Integer id);

    // 根据评论id查询评论
    @Select("SELECT c.*,l.avatar as avatar, l.nickname as nickname, r.nickname as replyUserNickname   " +
            "FROM article_comment c, `user` l, `user` r   " +
            "WHERE c.user_id=l.id and c.reply_user_id=r.id and c.id=#{id}")
    ArticleCommentDto selectChildByCommentId(Integer id);

    @Select("SELECT d.*,u.avatar as avatar, u.nickname as nickname  " +
            "FROM `article_comment` d, `user` u  " +
            "where d.user_id=u.id and d.id=#{id}")
    ArticleCommentDto selectParentByCommentId(Integer id);

    // 查询更多父亲评论
    @Select("SELECT d.*,u.avatar as avatar, u.nickname as nickname  " +
            "FROM `article_comment` d, `user` u  " +
            "where d.user_id=u.id and d.article_id=#{articleId} and d.parent_id=-1  and d.id < #{id} " +
            "ORDER BY d.id DESC   " +
            "LIMIT 8  ")
    List<ArticleCommentDto> selectMoreParentCommentByArticleId(ArticleComment articleComment);

    // 查询更多子评论
    @Select("SELECT c.*,l.avatar as avatar, l.nickname as nickname, r.nickname as replyUserNickname   " +
            "FROM article_comment c, `user` l, `user` r   " +
            "WHERE c.user_id=l.id and c.reply_user_id=r.id and c.parent_id=#{parentId} and c.id < #{id} " +
            "ORDER BY c.id DESC   " +
            "LIMIT 8")
    List<ArticleCommentDto> selectMoreChildCommentByParent(ArticleComment articleComment);

    @Update("UPDATE `article_comment` SET `like_number`=`like_number` + 1 WHERE id=#{id}")
    void addLikeById(Integer id);
    @Update("UPDATE `article_comment` SET `like_number`=`like_number` - 1 WHERE id=#{id}")
    void cancelLikeById(Integer id);
}
