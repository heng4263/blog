package com.example.kkblog.config;

/**
 * 系统固定参数
 * @author xiaoke
 * @date 2024/4/17
 */
public class ConstParam {
    // 用户名正则
    public static final String USER_NAME_PATTERN = "^[a-zA-Z0-9]{1,15}$";
    // 昵称长度
    public static final int MAX_NICKNAME_LENGTH = 15;
    // 签名长度
    public static final int MAX_SIGNATURE_LENGTH = 120;

    // 网址正则表达式
    public static final String URL_PATTERN = "^(http|https)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";
    // 友链长度
    public static final int MAX_LINK_LENGTH = 250;
    // 密码正则
    public static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{6,20}$";
    // 验证码过期时间
    public static final int VER_CODE_EXPIRE_TIME = 5 * 60 * 1000; // 毫秒
    // 发布动态加积分
    public static final int DYNAMIC_SCORES = 50;
    // 发布文章积分
    public static final int ARTICLE_SCORES = 100;
}
