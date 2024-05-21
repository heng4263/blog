package com.example.kkblog.controller;

import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.User;
import com.example.kkblog.mapper.UserMapper;
import com.example.kkblog.service.EmailService;
import com.example.kkblog.util.IPUtil;
import com.example.kkblog.util.JwtTokenUtil;
import com.example.kkblog.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author xiaoke
 * @date 2024/4/7
 */
@Controller
@RequestMapping("/login")
public class loginController {

    @Value("${kkBlog.cookie-expire}")
    private int cookieExpire;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping("")
    public String loginPage() {
        return "login";
    }

    @ResponseBody
    @PostMapping("/post")
    public ResponseDto loginPost(User user, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return ResponseDto.Fail("用户名或者密码为空！");
        }
        User user1 = userMapper.selectUserByUsername(user.getUsername());
        if (user1 == null || !user.getPassword().equals(user1.getPassword())) {
            return ResponseDto.Fail("用户未注册或者密码错误！");
        }
        String ipAddr = IPUtil.getClientIpAddr(request);
        String ipLocation = IPUtil.getIpLocation(ipAddr);
        user1.setIpAddress(ipAddr);
        user1.setIpLocation(ipLocation);
        session.setAttribute("user", userMapper.selectUserDtoByUserId(user1.getId()));
        userMapper.updateById(user1);
        // 设置cookie
        if (user.getRememberMe()) {
            String token = jwtTokenUtil.generateToken(String.valueOf(user1.getId()));
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(cookieExpire); // 设置Cookie过期时间为24小时
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return ResponseDto.Success("登录成功！", userMapper.selectUserDtoByUserId(user1.getId()));
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response){
        session.removeAttribute("user");
        // 创建一个Cookie对象，设置Cookie的名称和路径
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0); // 设置MaxAge为0，表示删除该Cookie
        cookie.setPath("/"); // 设置Cookie的路径

        // 将Cookie添加到响应中
        response.addCookie(cookie);
        return "login";
    }
}
