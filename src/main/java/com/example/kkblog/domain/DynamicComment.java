package com.example.kkblog.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 动态评论
 * @author xiaoke
 * @date 2024/4/7
 */
@Data
@TableName("dynamic_comment")
@Entity
public class DynamicComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private Integer id;
    // 父节点id
    private Integer parentId;
    // 动态id
    private Integer dynamicId;
    // 回复用户id
    private Integer replyUserId;
    // 回复内容
    private String replyContent;
    // 用户id
    private Integer userId;
    // 评论内容
    private String content;
    // ip地址
    private String ipLocation;
    // 点赞数量
    @TableField(fill = FieldFill.INSERT)
    private Integer likeNumber;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
}
