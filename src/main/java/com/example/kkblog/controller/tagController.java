package com.example.kkblog.controller;

import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.mapper.TagMapper;
import com.example.kkblog.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author xiaoke
 * @Date 2024 04 11 00 11
 **/
@Controller
@RequestMapping("/tag")
public class tagController {
    @Autowired
    private TagMapper tagMapper;
    // 获取推荐的标签
    @ResponseBody
    @GetMapping("/recommend")
    public ResponseDto recommend(@RequestParam(value = "key", required = false) String key) {
        if (StringUtils.isNull(key)) {
            key = "";
        }
        return ResponseDto.Success("查询成功！", tagMapper.selectRecommendTags(key));
    }
}
