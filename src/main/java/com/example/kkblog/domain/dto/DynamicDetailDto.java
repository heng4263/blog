package com.example.kkblog.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * @Author xiaoke
 * @Date 2024 04 12 22 52
 **/
@Data
@TableName("dynamic")
public class DynamicDetailDto {
    private Integer id;
    // 类型 1动态 2话题
    private Integer type;
    // 标题
    private String title;
    // 动态内容
    private String dynamic;
    // 话题内容
    private String topicContent;
    // 话题id
    private Integer topicId;
    // 话题名称
    private String topicName;
    // 标签
    private String tags;
    // 用户id
    private Integer userId;
    // 用户头像
    private String avatar;
    // 用户昵称
    private String nickname;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    // 浏览量
    private Integer viewNumber;
    // 点赞量
    private Integer likeNumber;
    // 收藏量
    private Integer starNumber;
    // 评论量
    private Integer commentNumber;
    // ip省份
    private String ipLocation;
    // 图片
    private String imageList;
    // 是否点赞
    @TableField(exist = false)
    private Boolean liked;
    // 是否收藏
    @TableField(exist = false)
    private Boolean stared;

}
