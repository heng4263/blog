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
@TableName("star")
@Entity
@Table(name = "star")
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private Integer id;
    // 用户id
    private Integer userId;
    // 类型 0动态 1文章
    private Integer type;
    // 动态id
    private Integer dynamicId;
    // 文章id
    private Integer articleId;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    // 快速搜索 userId_dynamicId;
    private String userAndDynamicId;
    // 快速搜索 userId_articleId;
    private String userAndArticleId;
}
