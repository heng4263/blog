package com.example.kkblog.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Hyh
 * @date 2024/4/29
 */
@Data
@TableName("article")
public class ArticlePreDto {
    private Integer id;
    // 用户id
    private Integer userId;
    // 用户昵称
    private String nickname;
    // 用户头像
    private String avatar;
    // 标题
    private String title;
    // 预览
    private String preview;
    // 封面
    private String cover;
    // 标签
    private String tags;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    // 更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    // 状态
    private Integer status;
    // 点赞数
    private Integer likeNumber;
    // 收藏数量
    private Integer starNumber;
    // 浏览数量
    private Integer viewNumber;
    // ip省份
    private String ipLocation;
    // 评论数量
    private Integer commentNumber;
    // 是否点赞
    @TableField(exist = false)
    private Boolean liked = false;
}
