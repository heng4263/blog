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
@TableName("user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private Integer id;
    // 邮箱
    private String email;
    // 账户名
    private String username;
    // 昵称
    private String nickname;
    // 性别
    private String gender;
    // 签名
    @TableField(fill = FieldFill.INSERT)
    private String signature;
    // 密码
    private String password;
    // 头像
    private String avatar;
    // 积分
    @TableField(fill = FieldFill.INSERT)
    private Integer scores;
    // 友链
    private String link;
    // 主页背景图
    private String backgroundImage;
    // ip地址
    private String ipAddress;
    // ip省份
    private String ipLocation;
    // 注册排名
    private Integer registerRank;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    // 更新时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;
    // 账户状态
    private Integer status;
    // 记住我
    @Transient
    @TableField(exist = false)
    private Boolean rememberMe;
    @Transient
    @TableField(exist = false)
    private String confirmPassword;
    @Transient
    @TableField(exist = false)
    private String verifyCode;
}
