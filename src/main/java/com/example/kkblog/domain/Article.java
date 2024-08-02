package com.example.kkblog.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Hyh
 * @date 2024/4/29
 */
@Data
@TableName("article")
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private Integer id;
    // 用户id
    private Integer userId;
    // 标题
    private String title;
    // 预览
    @Column(columnDefinition = "LONGTEXT")
    private String preview;
    // 正文
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    // 封面
    private String cover;
    // 标签
    private String tags;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    // 更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    // 状态
    private Integer status;
    // 是否置顶
    @TableField(fill = FieldFill.INSERT)
    private Integer stick;
    // 点赞数
    @TableField(fill = FieldFill.INSERT)
    private Integer likeNumber;
    // 收藏数量
    @TableField(fill = FieldFill.INSERT)
    private Integer starNumber;
    // 浏览数量
    @TableField(fill = FieldFill.INSERT)
    private Integer viewNumber;
    // ip省份
    private String ipLocation;
}
