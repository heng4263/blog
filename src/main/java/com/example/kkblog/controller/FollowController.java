package com.example.kkblog.controller;

import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.Follow;
import com.example.kkblog.domain.dto.UserDto;
import com.example.kkblog.mapper.FollowMapper;
import com.example.kkblog.service.KKBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author xiaoke
 * @date 2024/4/17
 */
@Controller
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowMapper followMapper;

    // 关注用户
    @ResponseBody
    @GetMapping("/doFollow")
    public ResponseDto doFollow(@RequestParam Integer userId, HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser == null) {
            return ResponseDto.Fail("请登录后进行操作");
        }
        if (Objects.equals(loginUser.getId(), userId)) {
            return ResponseDto.Fail("用户不能关注自己~");
        }
        Follow query = new Follow();
        query.setFollowedId(userId);
        query.setFollowerId(loginUser.getId());
        if (followMapper.selectIfFollowing(query) > 0) {
            return ResponseDto.Fail("你已经关注Ta咯~");
        }
        followMapper.insert(query);
        return ResponseDto.Success("操作成功！", query);
    }

    // 取消关注用户
    @ResponseBody
    @GetMapping("/undoFollow")
    public ResponseDto undoFollow(@RequestParam Integer userId, HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser == null) {
            return ResponseDto.Fail("请登录后进行操作");
        }
        Follow follow = new Follow();
        follow.setFollowedId(userId);
        follow.setFollowerId(loginUser.getId());
        if (followMapper.selectIfFollowing(follow) == 0) {
            return ResponseDto.Fail("你还没有关注Ta~");
        }
        followMapper.cancelFollow(follow);
        return ResponseDto.Success("操作成功！", 1);
    }
}
