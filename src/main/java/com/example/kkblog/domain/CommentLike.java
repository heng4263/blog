package com.example.kkblog.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 评论点赞
 */

/**
 * @author Hyh
 * @date 2024/4/15
 */
@Data
@TableName("comment_like")
@Entity
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private Integer id;
    // 点赞用户id
    private Integer userId;
    // 评论id
    private Integer commentId;
    // 用户和评论id（用于快速查询）
    private String userAndCommentId;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    // 被评论的动态id
    private Integer dynamicId;
    // 被点赞的用户id
    private Integer likedUserId;
}
