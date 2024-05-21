package com.example.kkblog.controller;

import com.example.kkblog.config.ConstParam;
import com.example.kkblog.config.exception.KKBlogException;
import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.User;
import com.example.kkblog.enmu.ACCOUNT_STATUS;
import com.example.kkblog.mapper.UserMapper;
import com.example.kkblog.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

/**
 * @Author xiaoke
 * @Date 2024 04 08 23 38
 **/
@Controller
@RequestMapping("/register")
public class registerController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("")
    public String register() {
        return "register";
    }

    @PostMapping("/post")
    @ResponseBody
    public ResponseDto registerPost(User user) throws KKBlogException {
        StringUtils.trimObject(user);
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new KKBlogException("请输入用户名！");
        }
        if(!user.getUsername().matches(ConstParam.USER_NAME_PATTERN)) {
            throw new KKBlogException("用户名仅支持英文字母加数字，长度1~15");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new KKBlogException("请输入密码！");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new KKBlogException("密码不一致！");
        }
        if (!user.getPassword().matches(ConstParam.PASSWORD_PATTERN)) {
            throw new KKBlogException("密码仅支持英文字母加数字，长度6~20");
        }
        if (StringUtils.isEmpty(user.getNickname())) {
            throw new KKBlogException("请输入昵称！");
        }
        if (user.getNickname().length() > ConstParam.MAX_NICKNAME_LENGTH) {
            throw new KKBlogException("昵称请不要超过" + ConstParam.MAX_NICKNAME_LENGTH + "个字");
        }
        // 校验用户名重复性
        if (userMapper.selectUserByUsername(user.getUsername()) != null) {
            throw new KKBlogException("该用户名已存在！");
        }
        // 注册排名
        Integer registerRank = userMapper.selectRegisterRank();
        registerRank = registerRank == null ? 1 : registerRank;
        user.setRegisterRank(registerRank);
        // 账号状态
        user.setStatus(ACCOUNT_STATUS.NORMAL.ordinal());
        // 设置头像
        Random random = new Random();
        int i = random.nextInt(10);
        user.setAvatar("/avatars/img_" + i + ".png");
        // 设置默认背景图片
        user.setBackgroundImage("/avatars/background/bg_0.png");
        userMapper.insert(user);
        return ResponseDto.Success("注册成功！", "");
    }
}
