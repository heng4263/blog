package com.example.kkblog.config.interceptors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kkblog.config.ConstParam;
import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.VerCode;
import com.example.kkblog.mapper.VerCodeMapper;
import com.example.kkblog.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author xiaoke
 * @Date 2024 04 17 23 26
 **/
@Configuration
public class VerCodeInterceptor implements HandlerInterceptor {
    @Autowired
    private VerCodeMapper verCodeMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = request.getParameter("key");
        String code = request.getParameter("code");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(code)) {
            response.getWriter().write(JSONObject.toJSONString(ResponseDto.Fail("请求失败：验证码错误！")));
            return false;
        }
        VerCode verCode = verCodeMapper.selectCodeByKey(key);
        if (verCode == null || !verCode.getCodeValue().equals(code)) {
            response.getWriter().write(JSON.toJSONString(ResponseDto.Fail("请求失败：验证码错误！")));
            return false;
        }
        // 校验有效期
        if (new Date().getTime() - verCode.getCreateTime().getTime() > ConstParam.VER_CODE_EXPIRE_TIME) {
            response.getWriter().write(JSON.toJSONString(ResponseDto.Fail("请求失败：验证码已过期！")));
            return false;
        }
        return true;
    }
}
