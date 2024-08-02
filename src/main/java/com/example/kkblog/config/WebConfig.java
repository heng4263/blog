package com.example.kkblog.config;

import com.example.kkblog.config.interceptors.CookieInterceptor;
import com.example.kkblog.config.interceptors.SessionInterceptor;
import com.example.kkblog.config.interceptors.VerCodeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpSession;

/**
 * @Author Hyh
 * @Date 2024 04 07 20 06
 **/


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CookieInterceptor cookieInterceptor;

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Autowired
    private VerCodeInterceptor verCodeInterceptor;

    @Value("${kkBlog.image-path}")
    private String imagePath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cookieInterceptor).addPathPatterns("/**"); // 拦截所有请求
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**").
                excludePathPatterns("/avatars/**", "/css/**", "editormd/**", "/extend/**", "/font/**", "/image_preview/**", "/jquery/**", "/layui/**", "/images/**",
                        "/login/**","/register/**", "/index/**", "/error", "/", "/index.html", "/code/**", "/iframe/userLaw");
        registry.addInterceptor(verCodeInterceptor)
                .addPathPatterns("/login/post", "/register/post");
    }

    // 内置头像
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("classpath:/static/avatars/");

        registry.addResourceHandler("/images/**").
                addResourceLocations("file:" + imagePath);
    }
}