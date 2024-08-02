package com.example.kkblog.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Hyh
 * @date 2024/4/7
 */
@Data
@TableName("image")
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private Integer id;
    // 用户id
    private Integer userId;
    // 图片名称
    private String name;
    // 图片 描述
    private String description;
    // 存储路径
    private String path;
    // 地址
    private String url;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
}
