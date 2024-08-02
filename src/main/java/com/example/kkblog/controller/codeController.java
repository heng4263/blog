package com.example.kkblog.controller;

import cn.hutool.core.codec.Base64Encoder;
import com.alibaba.fastjson.JSONObject;
import com.example.kkblog.config.exception.KKBlogException;
import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.VerCode;
import com.example.kkblog.mapper.VerCodeMapper;
import com.example.kkblog.service.EmailService;
import com.example.kkblog.util.StringUtils;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 验证码
 * @Author Hyh
 * @Date 2024 04 17 23 13
 **/
@RestController
@RequestMapping("/code")
public class codeController {

    @Autowired
    private DefaultKaptcha kaptcha;

    @Autowired
    private VerCodeMapper verCodeMapper;

    @Autowired
    private EmailService emailService;

    // 是否开启邮箱验证码
    @Value("${kkBlog.email-code}")
    public Boolean emailCodeOn;

    // 是否开启登录验证码
    @Value("${kkBlog.login-code}")
    private Boolean loginCode;

    // 获取图片验证码
    @GetMapping("/getCode")
    public ResponseDto getCode() throws IOException {
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        String createText = kaptcha.createText();//生成随机字母+数字(4位)
        BufferedImage challenge = kaptcha.createImage(createText);//根据文本构建图片
        ImageIO.write(challenge, "jpg", jpegOutputStream);
        byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        String base64 = Base64Encoder.encode(captchaChallengeAsJpeg);
        String key = UUID.randomUUID().toString();
        VerCode code = new VerCode();
        code.setCodeKey(key);
        code.setCodeValue(createText);
        verCodeMapper.insert(code);
        JSONObject data = new JSONObject();
        data.put("key", key);
        if (loginCode) {
            data.put("code",  "data:image/gif;base64," + base64);
            return ResponseDto.Success("获取验证码成功！", data);
        }
        else {
            data.put("code", "data:image/gif;base64," + base64);
            data.put("value", createText);
            data.put("type", 1);
            return ResponseDto.Success("验证码发送成功！", data);
        }
    }

    // 获取邮箱验证码
    @GetMapping("/getEmail")
    public ResponseDto getEmail(@RequestParam String email) {
        if (StringUtils.isEmpty(email)) {
            return ResponseDto.Fail("请输入邮箱!");
        }
        if (!StringUtils.checkEmail(email)) {
            throw new KKBlogException("请输入正确的邮箱格式!");
        }
        String verCode = kaptcha.createText();//生成随机字母+数字(4位)
        String key = UUID.randomUUID().toString();
        VerCode code = new VerCode();
        code.setCodeKey(key);
        code.setCodeValue(verCode);
        verCodeMapper.insert(code);
        JSONObject data = new JSONObject();
        data.put("key", key);
        if (emailCodeOn) {
            emailService.sendActivateHtml(email, verCode);
            return ResponseDto.Success("验证码发送成功，请查看您的邮箱！", data);
        }
        else {
            data.put("code", verCode);
            data.put("type", 1);
            return ResponseDto.Success("验证码发送成功！", data);
        }
    }
}
