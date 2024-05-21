package com.example.kkblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author xiaoke
 * @Date 2024 04 15 19 29
 **/
@Controller
public class redirectController {
    @GetMapping({"/", "/index.html"})
    public String redirect() {
        return "redirect:/index";
    }
}
