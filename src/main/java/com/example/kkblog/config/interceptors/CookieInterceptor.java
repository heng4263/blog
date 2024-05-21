package com.example.kkblog.config.interceptors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.kkblog.domain.User;
import com.example.kkblog.domain.dto.UserDto;
import com.example.kkblog.mapper.UserMapper;
import com.example.kkblog.util.IPUtil;
import com.example.kkblog.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class CookieInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        Object sessionUser = session.getAttribute("user");
        if (sessionUser == null) {
            // 从请求中获取Cookie
            Cookie[] cookies = request.getCookies();
            Object user = request.getAttribute("user");
            if (null == user) {
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("token".equals(cookie.getName())) {
                            if (jwtTokenUtil.validateToken(cookie.getValue())) {
                                UserDto userDto = userMapper.selectUserDtoByUserId(Integer.valueOf(jwtTokenUtil.getUserIdFromJWT(cookie.getValue())));
                                if (userDto == null) {
                                    return false;
                                }
                                User saveUser = new User();
                                String ipAddr = IPUtil.getClientIpAddr(request);
                                String ipLocation = IPUtil.getIpLocation(ipAddr);
                                saveUser.setIpAddress(ipAddr);
                                saveUser.setIpLocation(ipLocation);
                                saveUser.setId(userDto.getId());
                                // 更新用户ip地址
                                userMapper.updateById(saveUser);
                                // 设置到Model中
                                session.setAttribute("user", userDto);
                            }
                        }
                    }
                }
            }
        }
        // 继续执行下一个拦截器或处理器
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            HttpSession session = request.getSession();
            modelAndView.addObject("user", session.getAttribute("user"));
        }
        // 可以在这里对ModelAndView进行操作，如果需要的话
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求处理完毕后的操作，例如资源清理等
    }
}