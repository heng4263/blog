package com.example.kkblog.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 文章评论点赞
 */

/**
 * @author Hyh
 * @date 2024/4/15
 */
@Data
@TableName("article_comment_like")
@Entity
public class ArticleCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private Integer id;
    // 点赞用户id
    private Integer userId;
    // 评论id
    private Integer commentId;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    // 被评论的文章id
    private Integer articleId;
    // 被点赞的用户id
    private Integer likedUserId;
}
