package com.example.kkblog.controller;

import com.example.kkblog.config.ConstParam;
import com.example.kkblog.config.exception.KKBlogException;
import com.example.kkblog.controller.dto.PasswordDto;
import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.User;
import com.example.kkblog.domain.dto.UserDto;
import com.example.kkblog.mapper.UserMapper;
import com.example.kkblog.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Hyh
 * @date 2024/4/17
 */
@Controller
@RequestMapping("/profile")
public class profileController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/me")
    public String mePage() {
        return "user-profile";
    }

    // 更新用户信息
    @PostMapping("/update")
    @ResponseBody
    public ResponseDto updateUser(User user, @NotNull HttpSession session) throws KKBlogException {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        StringUtils.trimObject(user);
        if (StringUtils.isEmpty(user.getNickname())) {
            return ResponseDto.Fail("请输入昵称");
        }
        if (user.getNickname().length() > ConstParam.MAX_NICKNAME_LENGTH) {
            return ResponseDto.Fail("昵称请不要超过" + ConstParam.MAX_NICKNAME_LENGTH + "个字");
        }
        if (!StringUtils.isEmpty(user.getSignature()) && user.getSignature().length() > ConstParam.MAX_SIGNATURE_LENGTH) {
            return ResponseDto.Fail("签名请不要超过" + ConstParam.MAX_SIGNATURE_LENGTH + "个字");
        }
        if (!StringUtils.isEmpty(user.getLink()) && !user.getLink().matches(ConstParam.URL_PATTERN)) {
            return ResponseDto.Fail("请输入正确格式的友情链接");
        }
        if (!StringUtils.isEmpty(user.getLink()) && user.getLink().length() > ConstParam.MAX_LINK_LENGTH) {
            return ResponseDto.Fail("友链请不要超过" + ConstParam.MAX_LINK_LENGTH + "个字符");
        }
        User updateUser = userMapper.selectById(loginUser.getId());
        updateUser.setNickname(user.getNickname());
        updateUser.setGender(user.getGender());
        updateUser.setSignature(user.getSignature());
        updateUser.setLink(user.getLink());
        userMapper.updateById(updateUser);
        // 更新session
        session.setAttribute("user", userMapper.selectUserDtoByUserId(loginUser.getId()));
        return ResponseDto.Success("修改成功！", user);
    }

    // 修改密码
    @PostMapping("/password")
    @ResponseBody
    public ResponseDto updatePassword(PasswordDto passwordDto, HttpSession session) throws KKBlogException {
        StringUtils.trimObject(passwordDto);
        UserDto loginUser = (UserDto) session.getAttribute("user");
        User user = userMapper.selectById(loginUser.getId());
        if (!user.getPassword().equals(passwordDto.getOldPassword())) {
            return ResponseDto.Fail("旧密码错误！");
        }
        if (StringUtils.isEmpty(passwordDto.getNewPassword())) {
            return ResponseDto.Fail("请输入有效的新密码！");
        }
        if (passwordDto.getOldPassword().equals(passwordDto.getNewPassword())) {
            return ResponseDto.Fail("新旧密码不能相同！");
        }
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            return ResponseDto.Fail("新密码不一致");
        }
        if(!passwordDto.getNewPassword().matches(ConstParam.PASSWORD_PATTERN)) {
            return ResponseDto.Fail("密码仅支持英文字母加数字，长度6~20");
        }
        user.setPassword(passwordDto.getNewPassword());
        userMapper.updateById(user);
        return ResponseDto.Success("修改成功！", null);
    }
}
