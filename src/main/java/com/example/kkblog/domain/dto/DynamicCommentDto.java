package com.example.kkblog.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author xiaoke
 * @Date 2024 04 13 13 16
 **/
@Data
@TableName("dynamicComment")
public class DynamicCommentDto {
    private Integer id;
    // 父节点id
    private Integer parentId;
    // 动态id
    private Integer dynamicId;
    // 用户id
    private Integer userId;
    // 评论内容
    private String content;
    // 点赞数量
    private Integer likeNumber;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    // 被回复人id
    private Integer replyUserId;
    // 被回复人昵称
    private String replyUserNickname;
    // 用户昵称
    private String nickname;
    // 用户头像
    private String avatar;
    // 被回复数量
    private Integer commentNumber;
    // 回复内容
    private String replyContent;
    // ip地址
    private String ipLocation;
    // 子评论
    @TableField(exist = false)
    private List<DynamicCommentDto> children;
    // 是否点过赞
    @TableField(exist = false)
    private Boolean liked;
}
