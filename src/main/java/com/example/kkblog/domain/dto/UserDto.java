package com.example.kkblog.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author xiaoke
 * @Date 2024 04 07 20 12
 **/
@Data
@TableName("user")
public class UserDto {
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
    private String signature;
    // 头像
    private String avatar;
    // 积分
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
    // 动态数量
    private Integer dynamicNumber;
    // 评论数量
    private Integer commentNumber;
    // 是否是自己
    @TableField(exist = false)
    private Boolean me = false;
    // 是否关注
    @TableField(exist = false)
    private Boolean follow = false;
    // 粉丝数量
    @TableField(exist = false)
    private Integer fanNumber;
    // 关注数量
    @TableField(exist = false)
    private Integer followNumber;
}
