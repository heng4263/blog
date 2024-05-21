package com.example.kkblog.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xiaoke
 * @date 2024/4/7
 */
@Data
@TableName("dynamic")
@Entity
public class Dynamic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private Integer id;
    // 类型 0动态 1话题
    private Integer type;
    // 标题
    private String title;
    // 预览文字
    private String preView;
    // 动态内容
    @Column(columnDefinition = "VARCHAR(5000)")
    private String dynamic;
    // 话题内容
    @Column(columnDefinition = "LONGTEXT")
    private String topicContent;
    // 话题id
    private Integer topicId;
    // 标签
    private String tags;
    // 用户id
    private Integer userId;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    // 更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    // 状态 0:正常 1：待审核 2：已删除
    private Integer status;
    // 是否置顶
    @TableField(fill = FieldFill.INSERT)
    private Integer stick;
    // 浏览量
    @TableField(fill = FieldFill.INSERT)
    private Integer viewNumber;
    // 点赞量
    @TableField(fill = FieldFill.INSERT)
    private Integer likeNumber;
    // 收藏量
    @TableField(fill = FieldFill.INSERT)
    private Integer starNumber;
    // ip省份
    private String ipLocation;
    // 图片
    @Column(columnDefinition = "VARCHAR(1500)")
    private String imageList;
}
