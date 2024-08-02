package com.example.kkblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Hyh
 * @Date 2024 04 17 20 10
 **/
@Controller
@RequestMapping("/iframe")
public class iframeController {
    @GetMapping("/password")
    public String password() {
        return "iframe/password";
    }

    @GetMapping("/userLaw")
    public String userLaw() {
        return "iframe/userLaw";
    }
}
