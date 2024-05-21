package com.example.kkblog.service;

import com.example.kkblog.config.exception.KKBlogException;
import com.example.kkblog.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class EmailService {
    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender; // JavaMailSender是Spring Email的核心组件，负责发送邮件

    @Value("${spring.mail.username}")
    private String from;

    @Value("${kkBlog.protocol}")
    private String protocol;

    @Value("${kkBlog.ip-address}")
    private String host;

    @Value("${server.port}")
    private String server_port;

    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage(); // MimeMessage用于封装邮件的相关信息
            MimeMessageHelper helper = new MimeMessageHelper(message); // MimeMessageHelper用于辅助构建MimeMessage对象
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // 不写true默认text格式发送邮件
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败："+ e.getMessage());
        }
    }

    public boolean sendActivateHtml(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage(); // MimeMessage用于封装邮件的相关信息
            MimeMessageHelper helper = new MimeMessageHelper(message); // MimeMessageHelper用于辅助构建MimeMessage对象
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("KKBlog用户注册验证码");
            helper.setText(getActivateHtmlContent(to, code), true); // 不写true默认text格式发送邮件
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败："+ e.getMessage());
            throw new KKBlogException(e.getMessage());
        }
        return true;
    }

    public boolean sendResetPasswordHtml(String to, String code){
        try {
            MimeMessage message = mailSender.createMimeMessage(); // MimeMessage用于封装邮件的相关信息
            MimeMessageHelper helper = new MimeMessageHelper(message); // MimeMessageHelper用于辅助构建MimeMessage对象
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("小白书————密码重置");
            helper.setText(getResetHtmlContent(to, code), true); // 不写true默认text格式发送邮件
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败："+ e.getMessage());
            throw new KKBlogException(e.getMessage());
        }
        return true;
    }
    public String getActivateHtmlContent(String to, String code){
        Context context = new Context();
        context.setVariable("codeType", "KKBlog验证码");
        context.setVariable("email", to);
        context.setVariable("title", "KKBlog注册验证码");
        context.setVariable("code", code);
        context.setVariable("date", DateUtils.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return templateEngine.process("RegisterTemplate.html", context);
    }

    public String getResetHtmlContent(String to, String code){
        Context context = new Context();
        context.setVariable("title", "密码重置");
        context.setVariable("email", to);
        context.setVariable("code", code);
        context.setVariable("date", DateUtils.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        context.setVariable("action", String.format("%s://%s:%s/%s", protocol, host, server_port, "reset-password"));
        return templateEngine.process("/email/ResetPasswordTemplate.html", context);
    }
}
